package org.springframework.samples.petclinic.dataMigration;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.model.MBaseEntity;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.MPet;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvet.MVet;
import org.springframework.samples.petclinic.dataMigration.mvet.VetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.MVisit;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class ConsistencyChecker {

    private int numberOfInconsistency = 0;
    HashFunction hf = Hashing.sha256();
    private static boolean flag = false;

    @Autowired
    private OwnerMRepository ownerMRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private VetRepository vetRepository;
    @Autowired
    private VetMRepository vetMRepository;

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PetMRepository petMRepository;

    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private VisitMRepository visitMRepository;

    @Autowired
    private MigrationServices migrationServices;

    @Scheduled(cron = "*/1 * * * * *")
    @Async("threadPoolTaskExecutor")
    public void check(){
    	if(flag) {
        numberOfInconsistency += checkOwners();
        numberOfInconsistency += checkVet();
        numberOfInconsistency += checkPets();
        numberOfInconsistency += checkVisits();
    	migrationServices.printBanner("No. inconsistencies found in total: " + numberOfInconsistency);
    	}
    }
    
    public static void setFlag() {
    	flag = true;
    }

    private int checkOwners(){
        migrationServices.printBanner("Checking for inconsistencies in 'owners'");
        int inconsistencies = 0;

        Map<Integer, Owner> actualCollection = ownerRepository.findAll().stream()
            .collect(Collectors.toMap(owner -> owner.getId(), owner -> owner));

        Map<String, MOwner> expectedCollections = ownerMRepository.findAll().stream()
            .collect(Collectors.toMap(owner -> owner.getId(), owner -> owner));


        for(Integer x : actualCollection.keySet()){
            Owner actual = actualCollection.get(x);
            MOwner migrated = expectedCollections.get(x.toString());

            if(!compareActualAndExpected(actual, migrated)){
                //inconsistencies
                inconsistencies ++;
                System.out.println("INCONSISTENCY FOUND, INSERTING AGAIN");
                ownerMRepository.save(migrationServices.convertOwnerToMOwner(actual));
            }
        }
        migrationServices.printBanner("No. inconsistencies found in owners: " + inconsistencies);
        return inconsistencies;
    }


    private int checkVet(){
        migrationServices.printBanner("Checkign for inconsistencies in 'vets'");

        int inconsistencies = 0;
        Collection<Vet> vetData = vetRepository.findAll();
        Collection<MVet> mVetData = vetMRepository.findAll();

        ArrayList<Vet> vets = new ArrayList<>(vetData);
        ArrayList<MVet> mVets = new ArrayList<>(mVetData);

        for(int i=0; i<vets.size(); i++){

            Vet original = vets.get(i);
            MVet migrated = mVets.get(i);

            if(!compareActualAndExpected(original, migrated)){
                inconsistencies++;
                System.out.println("INCONSISTENCY FOUND, INSERTING AGAIN");
                vetMRepository.save(migrationServices.convertVetToMVet(original));
            }
        }

        migrationServices.printBanner("No. inconsistencies found in vets: " + inconsistencies);
        return inconsistencies;
    }

    private int checkPets() {
        migrationServices.printBanner("Checking for inconsistencies in 'pets'");

        int inconsistencies = 0;
        Collection<Pet> petData = petRepository.findAll();
        Collection<MPet> mPetData = petMRepository.findAll();

        ArrayList<Pet> pets = new ArrayList<>(petData);
        ArrayList<MPet> mPets = new ArrayList<>(mPetData);

        for(int i=0; i<pets.size(); i++){

            Pet original = pets.get(i);
            MPet migrated = mPets.get(i);

            if(!compareActualAndExpected(original, migrated)){
                inconsistencies++;
                System.out.println("INCONSISTENCY FOUND, INSERTING AGAIN");
                MPet newMPet = migrationServices.convertPetToMPet(original);
                // setting the owner isn't done in convertPetToMPet, so do it outside of method call
                MOwner mOwner = migrationServices.convertOwnerToMOwner(original.getOwner());
                newMPet.setOwner(mOwner);
                petMRepository.save(newMPet);
            }
        }

        migrationServices.printBanner("No. inconsistencies found in pets: " + inconsistencies);
        return inconsistencies;
    }

    private int checkVisits() {
        migrationServices.printBanner("Checking for inconsistencies in 'visits'");

        int inconsistencies = 0;
        Collection<Visit> actualCollection = visitRepository.findAll();
        Collection<MVisit> expectedCollection = visitMRepository.findAll();

        ArrayList<Visit> visits = new ArrayList<>(actualCollection);
        ArrayList<MVisit> mVisits = new ArrayList<>(expectedCollection);

        for (int i=0; i<visits.size(); i++){

            Visit actual = visits.get(i);
            MVisit migrated = mVisits.get(i);

            if(!compareActualAndExpected(actual, migrated)){
                inconsistencies++;
                System.out.println("INCONSISTENCY FOUND, INSERTING AGAIN");
                visitMRepository.save(migrationServices.convertVisitToMvisit(actual));
            }
        }

        migrationServices.printBanner("No. inconsistencies found in Visits: " + inconsistencies);

        return inconsistencies;
    }

    private boolean compareActualAndExpected(BaseEntity a, MBaseEntity m){
        System.out.println("actual:" + a.toString());
        System.out.println("migrated:" + m.toString());

        HashCode codeOriginal = hf.newHasher()
            .putString(a.toString(), Charsets.UTF_8)
            .hash();

        HashCode codeMigrated = hf.newHasher()
            .putString(m.toString(), Charsets.UTF_8)
            .hash();

        Boolean consistent = codeOriginal.equals(codeMigrated);
        System.out.println("Consistent? -> "+consistent);
        System.out.println("\n");

        return consistent;
    }


}
