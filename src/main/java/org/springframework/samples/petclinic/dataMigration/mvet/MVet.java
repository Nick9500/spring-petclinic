package org.springframework.samples.petclinic.dataMigration.mvet;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.samples.petclinic.dataMigration.model.MPerson;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Document(collection = "vets")
public class MVet extends MPerson{

    private Set<MSpecialty> specialties = new HashSet<MSpecialty>();

    public void setSpecialties(Set<MSpecialty> specialties){
        this.specialties = specialties;
    }

    public void addSpecialty(MSpecialty mSpecialty){
        specialties.add(mSpecialty);
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
