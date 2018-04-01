package org.springframework.samples.petclinic.dataMigration.mvet;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.samples.petclinic.dataMigration.model.MPerson;


@Document(collection = "vets")
public class MVet extends MPerson{

    private int oldId;  //mysql ID

    public int getOldId(){return this.oldId;}

    public void setOldId(int oldId) {this.oldId = oldId;}


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
