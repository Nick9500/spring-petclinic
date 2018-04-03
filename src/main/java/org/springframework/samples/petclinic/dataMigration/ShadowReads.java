package org.springframework.samples.petclinic.dataMigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.MPet;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvet.MVet;
import org.springframework.samples.petclinic.dataMigration.mvet.VetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.MVisit;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ShadowReads {

    @Autowired
    private OwnerMRepository ownerMRepository;

    @Autowired
    private VetMRepository vetMRepository;

    @Autowired
    private PetMRepository petMRepository;

    @Autowired
    private VisitMRepository visitMRepository;

    @Autowired
    private MigrationServices migrationServices;

    @Autowired
    private ConsistencyChecker consistencyChecker;

    @Async("ShadowReadThread")
    public void findAllVets(Collection<Vet> original){
        Collection<MVet> migrated = vetMRepository.findAll();
        consistencyChecker.shadowReadConsistencyCheck(original, migrated);
    }

    @Async("ShadowReadThread")
    public void findPetByID(Pet originalPet, String petId) {
    	migrationServices.printBanner("Shadowing reading on thread: "+Thread.currentThread().getName()+" for Pet By ID: "+petId);
    	MPet migratedPet = petMRepository.findById(petId).get();
    	consistencyChecker.shadowReadConsistencyCheck(originalPet, migratedPet);
    }

    @Async("ShadowReadThread")
    public void findOwnerByLastName(Collection<Owner> actualResults, String lastName){
        migrationServices.printBanner("Shadowing reading on thread: "+Thread.currentThread().getName()+" for Owner By Last Name: "+lastName);
        Map<Integer, Owner> actual = actualResults.stream().collect(Collectors.toMap(owner -> owner.getId(), owner -> owner));
        List<MOwner> expectedResults = ownerMRepository.findByLastName(lastName);

        for(MOwner mowner : expectedResults){
            try{
                Owner owner = actual.get(Integer.parseInt(mowner.getId()));
                if(!consistencyChecker.compareActualAndExpected(owner, mowner)){
                    //inconsistent
                    System.out.printf("Inconsistent read from Mongo... fixing inconsistency");
                    ownerMRepository.deleteById(mowner.getId());
                    ownerMRepository.save(migrationServices.convertOwnerToMOwner(owner));
                }
            }catch(NullPointerException e){
                // extra data in the new db
                System.out.println("Inconsistency found: extra data in Mongodb... Now deleting the extra data");
                ownerMRepository.deleteById(mowner.getId());
            }
        }
    }
}
