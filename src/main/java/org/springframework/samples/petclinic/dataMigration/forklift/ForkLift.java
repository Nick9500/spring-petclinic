package org.springframework.samples.petclinic.dataMigration.forklift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mongoSampleIntegration.EmployeeRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
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
        System.out.println("Done forklifting");

    }
}
