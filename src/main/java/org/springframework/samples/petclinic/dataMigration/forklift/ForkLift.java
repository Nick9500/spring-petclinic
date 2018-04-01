package org.springframework.samples.petclinic.dataMigration.forklift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.MVisit;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ForkLift {
    @Autowired
    OwnerMRepository ownerMRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    VisitRepository visitRepository;

    @Autowired
    VisitMRepository visitMRepository;

    public void start(){
        System.out.println("Start forklifting...");
        ownerMRepository.deleteAll();
        visitMRepository.deleteAll();
        Collection<Owner> forkliftData = ownerRepository.findAll();
        for (Owner owner : forkliftData){

            //BROKEN AS FK
            MOwner mowner = convertOwnerToMOwner(owner);
            System.out.println(mowner);
            ownerMRepository.save(mowner);

        }

        Collection<Visit> forkliftedVisitData = visitRepository.findAll();

        for (Visit visit : forkliftedVisitData){
            MVisit mvisit = new MVisit();
            mvisit.setDate( visit.getDate() );
            mvisit.setDescription( visit.getDescription() );
            mvisit.setPetId( visit.getPetId().toString() );
            visitMRepository.save( mvisit );
        }

        System.out.println("Done forklifting");
    }

    private MOwner convertOwnerToMOwner(Owner owner){
        MOwner mowner = new MOwner();
        mowner.setId(owner.getId().toString());
        mowner.setFirstName(owner.getFirstName());
        mowner.setLastName(owner.getLastName());
        mowner.setAddress(owner.getAddress());
        mowner.setCity(owner.getCity());
        mowner.setTelephone(owner.getTelephone());
        return mowner;
    }

    }
}
