package org.springframework.samples.petclinic.dataMigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvet.VetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.VisitRepository;
import java.util.Collection;

public class ShadowReads {

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private OwnerMRepository ownerMRepository;

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

    @Autowired
    private ConsistencyChecker consistencyChecker;

    // shadow read for owner method findById
    public void OwnerFindById(Integer id,  String Mid){
        Owner original = ownerRepository.findById(id);
        MOwner migrated = ownerMRepository.findById(Mid).get();
         if(!(consistencyChecker.compareActualAndExpected(original, migrated))){
             System.out.println("INCONSISTENCY FOUND, INSERTING AGAIN");
             ownerMRepository.save(migrationServices.convertOwnerToMOwner(original));
         }
    }

    // shadow read for owner method finalAll
    public void OwnerFindAll(){
        Collection<Owner> original = ownerRepository.findAll();
        Collection<MOwner> migrated = ownerMRepository.findAll();

    }

    public void VetFindAll(){

    }

    public void PetFindAll(){

    }

    public void VisitFindAll(){

    }

}
