package org.springframework.samples.petclinic.dataMigration.mowner;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.samples.petclinic.dataMigration.model.MPerson;

import java.util.*;

@Document(collection="owners")
public class MOwner extends MPerson {

//    @NotEmpty
    private String address;

//    @NotEmpty
    private String city;

//    @NotEmpty
//    @Digits(fraction = 0, integer = 10)
    private String telephone;

//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "owner")
    private Set<MPet> pets;

    public MOwner() {

    }

    public MOwner(String firstName, String lastName, String address, String city, String telephone, Set<MPet> pets) {
        super(firstName,lastName);
        this.address = address;
        this.city = city;
        this.telephone = telephone;
        this.pets = pets;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    protected Set<MPet> getPetsInternal() {
        if (this.pets == null) {
            this.pets = new HashSet<>();
        }
        return this.pets;
    }

    public void setPets(Set<MPet> pets) {
        this.pets = pets;
    }

    public List<MPet> getPets() {
        List<MPet> sortedPets = new ArrayList<>(getPetsInternal());
        PropertyComparator.sort(sortedPets,
            new MutableSortDefinition("name", true, true));
        return Collections.unmodifiableList(sortedPets);
    }

    public void addPet(MPet pet) {
        if (pet.isNew()) {
            getPetsInternal().add(pet);
        }
        pet.setOwner(this);
    }

    /**
     * Return the Pet with the given name, or null if none found for this Owner.
     *
     * @param name to test
     * @return true if pet name is already in use
     */
    public MPet getPet(String name) {
        return getPet(name, false);
    }

    /**
     * Return the Pet with the given name, or null if none found for this Owner.
     *
     * @param name to test
     * @return true if pet name is already in use
     */
    public MPet getPet(String name, boolean ignoreNew) {
        name = name.toLowerCase();
        for (MPet pet : getPetsInternal()) {
            if (!ignoreNew || !pet.isNew()) {
                String compName = pet.getName();
                compName = compName.toLowerCase();
                if (compName.equals(name)) {
                    return pet;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append("id:"+this.getId())
            .append("lastName:"+ this.getLastName())
            .append("firstName:"+this.getFirstName())
            .append("address:"+this.address)
            .append("city:"+this.city)
            .append("telephone:"+this.telephone).toString();
    }
}
