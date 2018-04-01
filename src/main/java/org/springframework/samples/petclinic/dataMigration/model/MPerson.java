package org.springframework.samples.petclinic.dataMigration.model;

import javax.validation.constraints.NotEmpty;

public class MPerson extends MBaseEntity {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
