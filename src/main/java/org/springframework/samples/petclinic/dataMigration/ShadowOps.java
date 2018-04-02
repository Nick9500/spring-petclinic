package org.springframework.samples.petclinic.dataMigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvet.VetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.MVisit;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
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

    public boolean shadowWrite( Visit visit, MVisit mvisit ) {
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

    public boolean shadowWriterConsistencyCheck(Visit old, MVisit fresh)
    {
        return cc.compareActualAndExpected(old, fresh);
    }


}
