package org.springframework.samples.petclinic.dataMigration.mowner;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.samples.petclinic.dataMigration.model.MNamedEntity;
import org.springframework.samples.petclinic.dataMigration.mvisit.MVisit;
import org.springframework.samples.petclinic.owner.PetType;

import java.util.*;

@Document(collection="pets")
public class MPet extends MNamedEntity {

    private Date birthDate;

    private PetType type;

    private MOwner owner;

    private Set<MVisit> visits = new LinkedHashSet<>();

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public PetType getType() {
        return this.type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public MOwner getOwner() {
        return this.owner;
    }

    public void setOwner(MOwner owner) {
        this.owner = owner;
    }

    protected Set<MVisit> getVisitsInternal() {
        if (this.visits == null) {
            this.visits = new HashSet<>();
        }
        return this.visits;
    }

    protected void setVisitsInternal(Set<MVisit> visits) {
        this.visits = visits;
    }

    public List<MVisit> getVisits() {
        List<MVisit> sortedVisits = new ArrayList<>(getVisitsInternal());
        PropertyComparator.sort(sortedVisits,
            new MutableSortDefinition("date", false, false));
        return Collections.unmodifiableList(sortedVisits);
    }

    public void addVisit(MVisit visit) {
        getVisitsInternal().add(visit);
        visit.setPetId(this.getId());
    }
}
