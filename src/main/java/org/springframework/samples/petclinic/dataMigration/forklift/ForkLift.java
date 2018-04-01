package org.springframework.samples.petclinic.dataMigration.forklift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mvet.MSpecialty;
import org.springframework.samples.petclinic.dataMigration.mvet.MVet;
import org.springframework.samples.petclinic.dataMigration.mvet.VetMRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.stereotype.Component;

import javax.management.Query;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class ForkLift {
    @Autowired
    OwnerMRepository ownerMRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    VetRepository vetRepository;
    @Autowired
    VetMRepository vetMRepository;

    public void start(){
        System.out.println("Start forklifting...");
        ownerMRepository.deleteAll();

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
        System.out.println("Done forklifting Owners");

        vetForklift();
    }


    private void vetForklift(){
        System.out.println("Start vets forklift");
        vetMRepository.deleteAll();
        Collection<Vet> vetData = vetRepository.findAll();
        for(Vet v: vetData){
            MVet mVet = new MVet();
            mVet.setId(v.getId().toString());
            mVet.setFirstName(v.getFirstName());
            mVet.setLastName(v.getLastName());
            List<Specialty> specialties = v.getSpecialties();
            if(!specialties.isEmpty()){
                for(Specialty s: specialties){
                    MSpecialty newMSpecialty = new MSpecialty();
                    newMSpecialty.setName(s.getName());
                    mVet.addSpecialty(newMSpecialty);
                }
            }
            vetMRepository.save(mVet);
        }

        Collection<Specialty> specialtyData = new Vet().getSpecialties();

        System.out.println("Done vets forklift");
    }
}
