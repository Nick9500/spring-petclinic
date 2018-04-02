package org.springframework.samples.petclinic.dataMigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvet.VetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
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
        cc.shadowWriteConsistencyCheck(owner);
    }

    public void save(@Valid Pet pet) {

    }

    public void save(@Valid Vet vet) {
    }

    public void save(@Valid Visit visit) {
    }


}
