package org.springframework.samples.petclinic.dataMigration.mvet;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.samples.petclinic.dataMigration.model.MPerson;
import org.springframework.samples.petclinic.vet.Specialty;

import javax.xml.bind.annotation.XmlElement;
import java.util.*;


@Document(collection = "vets")
public class MVet extends MPerson{

    private List<MSpecialty> specialties = new ArrayList<>();

    public void setSpecialties(List<MSpecialty> specialties){
        this.specialties = specialties;
    }

    public void addSpecialty(MSpecialty mSpecialty){
        specialties.add(mSpecialty);
    }

    @XmlElement
    public List<MSpecialty> getSpecialties() {
        List<MSpecialty> sortedSpecs = specialties;
        PropertyComparator.sort(sortedSpecs,
            new MutableSortDefinition("name", true, false));
        return Collections.unmodifiableList(sortedSpecs);
    }

    public int getNrOfSpecialties() {
        return specialties.size();
    }


    @Override
    public String toString() {
        String s = "Id: " + this.getId() + " FirstName: " + this.getFirstName() + " LastName: " + this.getLastName() + " specialties=" + specialties.toString() + "}";
        return s;
    }

}
