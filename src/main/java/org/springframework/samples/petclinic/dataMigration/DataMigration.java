package org.springframework.samples.petclinic.dataMigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class DataMigration {

    @Autowired
    private ForkLift forkLift;

    @Autowired
    private ConsistencyChecker consistencyChecker;

    public void start() {
        System.out.println("Data Migration to MongoDb Starting...");
        forkLift.start();

        try{
            activateConsistencyCheck();
        }catch(InterruptedException e){
            System.out.println("Tell the highly trained monkeys to fix this");
            System.out.println(e);
        }
        catch(ExecutionException e){
            System.out.println("Tell the highly trained monkeys to fix this");
            System.out.println(e);

        }
    }

    public void activateConsistencyCheck() throws InterruptedException, ExecutionException {
        System.out.println("Invoking the asynchronous consistency Check: " + Thread.currentThread().getName());
        Future<Integer> future = consistencyChecker.check();

        /*
            you can do things in parallel while the async call is getting called
         */

        while (true) {
            if (future.isDone()) {
                System.out.println("Total No. Of Inconsistencies in MongoDb - " + future.get());
                break;
            }

            Thread.sleep(10000);
        }
    }
}
