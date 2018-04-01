package org.springframework.samples.petclinic.dataMigration.forklift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ForkLift {
    @Autowired
    OwnerMRepository ownerMRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    PetRepository petRepository;

    public void start(){
        System.out.println("Start forklifting...");
        ownerMRepository.deleteAll();

        //owners
        Collection<Owner> forkliftData = ownerRepository.findAll();
        for (Owner owner : forkliftData){

            //BROKEN AS FK
            MOwner mowner = new MOwner();
            mowner.setId(owner.getId().toString());
            mowner.setFirstName(owner.getFirstName());
            mowner.setLastName(owner.getLastName());
            mowner.setAddress(owner.getAddress());
            mowner.setCity(owner.getCity());
            mowner.setTelephone(owner.getTelephone());
            System.out.println(mowner);
            ownerMRepository.save(mowner);

        }
        //pets
        Collection<Pet> forkliftDataPets = petRepository.findAll();
        for (Pet pet : forkliftDataPets){
            System.out.println(pet);
        }

        System.out.println("Finished forklifting....");
    }


}
