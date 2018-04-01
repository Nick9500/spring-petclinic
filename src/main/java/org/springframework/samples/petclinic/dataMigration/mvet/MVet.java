package org.springframework.samples.petclinic.dataMigration.mvet;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.samples.petclinic.dataMigration.model.MPerson;

import java.util.*;


@Document(collection = "vets")
public class MVet extends MPerson{

    private List<MSpecialty> specialties = new ArrayList<MSpecialty>();

    public void setSpecialties(List<MSpecialty> specialties){
        this.specialties = specialties;
    }

    public void addSpecialty(MSpecialty mSpecialty){
        specialties.add(mSpecialty);
    }

    @Override
    public String toString() {
        String s = "Id: " + this.getId() + " FirstName: " + this.getFirstName() + " LastName: " + this.getLastName() + " specialties=" + specialties.toString() + "}";
        return s;
    }


//    private Set<Specialty> specialties;
//
//    protected Set<Specialty> getSpecialtiesInternal() {
//        if (this.specialties == null) {
//            this.specialties = new HashSet<>();
//        }
//        return this.specialties;
//    }

//    protected void setSpecialtiesInternal(Set<Specialty> specialties) {
//        this.specialties = specialties;
//    }
//
//    @XmlElement
//    public List<Specialty> getSpecialties() {
//        List<Specialty> sortedSpecs = new ArrayList<>(getSpecialtiesInternal());
//        PropertyComparator.sort(sortedSpecs,
//            new MutableSortDefinition("name", true, true));
//        return Collections.unmodifiableList(sortedSpecs);
//    }
//
//    public int getNrOfSpecialties() {
//        return getSpecialtiesInternal().size();
//    }
//
//    public void addSpecialty(Specialty specialty) {
//        getSpecialtiesInternal().add(specialty);
//    }

}
