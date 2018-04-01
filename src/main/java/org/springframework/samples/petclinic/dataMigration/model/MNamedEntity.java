package org.springframework.samples.petclinic.dataMigration.model;

/**
 * Created by ericxiao on 2018-04-01.
 */
public class MNamedEntity extends MBaseEntity{

    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
