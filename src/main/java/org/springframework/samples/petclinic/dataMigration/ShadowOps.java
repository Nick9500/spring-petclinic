package org.springframework.samples.petclinic.dataMigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.model.MBaseEntity;
import org.springframework.samples.petclinic.dataMigration.mowner.MPet;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvet.VetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.MVisit;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ShadowOps {
    @Autowired
    private ConsistencyChecker cc;

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

    public boolean shadowWriteVisits( Visit visit, MVisit mvisit ) {
        shadowWriteOldVisit( visit );
        shadowWriteNewMVisit( mvisit );
        return shadowWriterConsistencyCheck(visit, mvisit);
    }

    public void shadowWriteOldVisit( Visit visit ){
        visitRepository.save( visit );
    }

    public void shadowWriteNewMVisit( MVisit mvisit ){
        visitMRepository.save( mvisit );
    }


    public boolean shadowWritePets(Pet pet, MPet mPet) {
//        shadowWriteOldPet( pet );
//        shadowWriteNewMPet( mPet );
        saveAsync(pet);
        saveAsync(mPet);
        return shadowWriterConsistencyCheck(pet, mPet);
    }

    @Async
    public void shadowWriteOldPet( Pet pet ){
        System.out.println("Shadow writing to pets!!!");
        petRepository.save( pet );
        System.out.println("saved to pets");
    }
    @Async
    public void shadowWriteNewMPet( MPet mpet ){
        System.out.println("Shadow writing to Mpets!!!");
        petMRepository.save( mpet );
        System.out.println("saved to mpets");
    }

    @Async
    public void saveAsync(Object somePet){
        System.out.println("SAVING OF CLASS TYPE " + somePet.getClass().toString());
        if(somePet instanceof Pet) {
            this.petRepository.save((Pet) somePet);
            System.out.println("saved to pets");
        }
        else if(somePet instanceof MPet){
            this.petMRepository.save((MPet)somePet);
            System.out.println("saved to mpets");

        }
    }



    public boolean shadowWriterConsistencyCheck(BaseEntity old, MBaseEntity fresh)
    {
        return cc.compareActualAndExpected(old, fresh);
    }


}
