package org.springframework.samples.petclinic.dataMigration.forklift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mongoSampleIntegration.EmployeeRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mvet.MVet;
import org.springframework.samples.petclinic.dataMigration.mvet.VetMRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ForkLift {
    @Autowired
    EmployeeRepository employeeRepository;

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
        employeeRepository.deleteAll();

        Collection<Owner> forkliftData = ownerRepository.findAll();
        for (Owner owner : forkliftData){
            //testing Saving objects into Employee table
//            employeeRepository.save(new Employee(owner.getFirstName(), owner.getLastName()));
//            System.out.println(employeeRepository.findByFirstName(owner.getFirstName()));

            //BROKEN AS FK
            MOwner mowner = new MOwner();
            mowner.setFirstName(owner.getFirstName());
            mowner.setFirstName(owner.getLastName());
            mowner.setAddress(owner.getAddress());
            mowner.setCity(owner.getCity());
            mowner.setTelephone(owner.getTelephone());

            ownerMRepository.save(mowner);

        }

        vetForklift();

        System.out.println("Done forklifting");

    }


    private void vetForklift(){
        Collection<Vet> data = vetRepository.findAll();
        for(Vet v: data){
            MVet mVet = new MVet();
            mVet.setFirstName(v.getFirstName());
            mVet.setLastName(v.getLastName());
            mVet.setOldId(v.getId());
            vetMRepository.save(mVet);
        }
    }
}
