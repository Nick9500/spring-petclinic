package org.springframework.samples.petclinic.dataMigration.mvisit;

import java.util.Date;

/**
 * Created by ericxiao on 2018-04-01.
 */
public class MVisit {

    private Date date;

    private String description;

    private String petId;

    /**
     * Creates a new instance of Visit for the current date
     */
    public MVisit() {
        this.date = new Date();
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPetId() {
        return this.petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }
}