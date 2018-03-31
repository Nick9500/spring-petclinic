package org.springframework.samples.petclinic.dataMigration.forklift;

import org.springframework.beans.factory.annotation.Autowired;
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

        //owners
        Collection<Owner> forkliftData = ownerRepository.findAll();
        for (Owner owner : forkliftData){
            System.out.println(owner);
        }

        //pets
        Collection<Pet> forkliftDataPets = petRepository.findAll();
        for (Pet pet : forkliftDataPets){
            System.out.println(pet);
        }

        System.out.println("Finished forklifting....");
    }


}
