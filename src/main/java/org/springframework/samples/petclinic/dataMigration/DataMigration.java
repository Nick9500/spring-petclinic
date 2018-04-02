package org.springframework.samples.petclinic.dataMigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataMigration {

    @Autowired
    private ForkLift forkLift;

    @Autowired
    private ConsistencyChecker consistencyChecker;

    public void start() {
        System.out.println("Data Migration to MongoDb Starting...");
        forkLift.start();
        //consistency check
        System.out.println("Consistency Checking active...");
        consistencyChecker.check();
    }
}
