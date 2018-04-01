package org.springframework.samples.petclinic.dataMigration.mongoSampleIntegration;

import org.springframework.data.annotation.Id;

/**
 * Created by ericxiao on 2018-03-31.
 */
public class Personx {
    @Id
    private String id;

    String lastName;

    public Personx(String lastName) {
        this.lastName = lastName;
    }
}
