package org.springframework.samples.petclinic.dataMigration.forklift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.MPet;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
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

    @Autowired
    PetMRepository petMRepository;

    public void start(){
        System.out.println("Start forklifting...");
        ownerMRepository.deleteAll();
        petMRepository.deleteAll();

        //owners
        Collection<Owner> forkliftData = ownerRepository.findAll();
        for (Owner owner : forkliftData){

            //BROKEN AS FK
            MOwner mowner = convertOwnerToMOwner(owner);
            System.out.println(mowner);
            ownerMRepository.save(mowner);

        }
        //pets
        Collection<Pet> forkliftDataPets = petRepository.findAll();
        forkliftPets(forkliftDataPets);
        System.out.println("Finished forklifting....");
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

    private void forkliftPets(Collection<Pet> pets){
        for (Pet pet : pets){
            MPet mpet = new MPet();
            mpet.setBirthDate(pet.getBirthDate());
            mpet.setType(pet.getType());
            MOwner mOwner = convertOwnerToMOwner(pet.getOwner());
            mpet.setOwner(mOwner);
            petMRepository.save(mpet);
        }

    }
}
