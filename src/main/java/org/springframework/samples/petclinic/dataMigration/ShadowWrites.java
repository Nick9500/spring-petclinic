package org.springframework.samples.petclinic.dataMigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mowner.MPet;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class ShadowWrites {
    @Autowired
    private OwnerMRepository ownerMRepository;

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
        migrationServices.printBanner("Shadow writing owner with thread: "+Thread.currentThread().getName());
        ownerMRepository.save(migrationServices.convertOwnerToMOwner(owner));
        cc.shadowWriteConsistencyCheck(owner);
    }

    @Async("ShadowWriteThread")
    public void save(@Valid Pet pet) {
        migrationServices.printBanner("Shadow writing pet with thread: "+Thread.currentThread().getName());

        MPet mpet = migrationServices.convertPetToMPet(pet);
        mpet.setOwner(migrationServices.convertOwnerToMOwner(pet.getOwner()));

        petMRepository.save(mpet);
        cc.shadowWriteConsistencyCheck(pet);
    }

    @Async("ShadowWriteThread")
    public void save(@Valid Visit visit) {
        migrationServices.printBanner("Shadow writing visit with thread: " + Thread.currentThread().getName());
        visitMRepository.save(migrationServices.convertVisitToMvisit(visit));
        cc.shadowWriteConsistencyCheck(visit);
    }
}
