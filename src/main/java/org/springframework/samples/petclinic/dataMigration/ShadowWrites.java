package org.springframework.samples.petclinic.dataMigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mowner.MPet;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvet.VetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class ShadowWrites {
    @Autowired
    private OwnerMRepository ownerMRepository;

    @Autowired
    private VetMRepository vetMRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetMRepository petMRepository;

    @Autowired
    private VisitMRepository visitMRepository;


    @Autowired
    private MigrationServices migrationServices;

    @Autowired
    private ConsistencyChecker cc;


    @Async("ShadowWriteThread")
    public void save(@Valid Owner owner) {
        migrationServices.printBanner("Shadow writing with thread: "+Thread.currentThread().getName());
        ownerMRepository.save(migrationServices.convertOwnerToMOwner(owner));
        cc.shadowWriteOwnerConsistencyCheck(owner);
    }

    @Async("ShadowWriteThread")
    public void save(@Valid Pet pet) {
        migrationServices.printBanner("Shadow writing with thread: "+Thread.currentThread().getName());
        MPet mpet = migrationServices.convertPetToMPet(pet);
        mpet.setId(String.valueOf(petRepository.findLastId()+1));
        System.out.println(pet.getOwner());
        mpet.setOwner(migrationServices.convertOwnerToMOwner(pet.getOwner()));
        petMRepository.save(mpet);

        if (!cc.compareActualAndExpected(pet, mpet)) {
            System.out.println("ShadowWrite Consistency Check has Failed");
        }
        System.out.println("Shadow Write Visit Success");
    }

    public void save(@Valid Vet vet) {
    }

    public void save(@Valid Visit visit) {
    }


}
