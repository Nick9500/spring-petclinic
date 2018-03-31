package org.springframework.samples.petclinic.dataMigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.forklift.ForkLift;
import org.springframework.stereotype.Component;

@Component
public class DataMigration {

    @Autowired
    ForkLift forkLift;

    public void start(){
        System.out.println("Data Migration to MongoDb Starting...");
        forkLift.start();
    }
}
