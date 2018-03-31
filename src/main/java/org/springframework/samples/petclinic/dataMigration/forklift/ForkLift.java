package org.springframework.samples.petclinic.dataMigration.forklift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ForkLift {
    @Autowired
    OwnerMRepository ownerMRepository;

    @Autowired
    OwnerRepository ownerRepository;

    public void start(){
        System.out.println("Start forklifting...");

        Collection<Owner> forkliftData = ownerRepository.findAll();
        for (Owner owner : forkliftData){
            System.out.println(owner);
        }
    }
}
