package org.springframework.samples.petclinic.dataMigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataMigration {

    @Autowired
    private ForkLift forkLift;

    public void start() {
        System.out.println("Data Migration to MongoDb Starting...");
        forkLift.start();

    }

}
