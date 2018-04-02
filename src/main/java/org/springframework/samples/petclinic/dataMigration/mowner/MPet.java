package org.springframework.samples.petclinic.dataMigration.mowner;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.samples.petclinic.dataMigration.model.MNamedEntity;
import org.springframework.samples.petclinic.dataMigration.mvisit.MVisit;
import org.springframework.samples.petclinic.owner.PetType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Document(collection="pets")
public class MPet extends MNamedEntity {
    private Date birthDate;

    private PetType type;

    private MOwner owner;

    private Set<MVisit> visits = new LinkedHashSet<>();

    public void setBirthDate(Date birthDate) {
        try{
            String date = birthDate.toString();
            System.out.println(date);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date formatDate = format.parse(date);
            this.birthDate = formatDate;
        }
        catch(ParseException e){
            System.out.println("Exception found");
        }
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

    @Override
    public String toString() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        return
            "name=" + this.getName() +
            ", birthDate=" + dt.format(birthDate) +
            ", type=" + type +
            ", owner=" + owner;
    }
}
