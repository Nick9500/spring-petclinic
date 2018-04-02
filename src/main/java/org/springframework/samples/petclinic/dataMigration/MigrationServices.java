package org.springframework.samples.petclinic.dataMigration;

import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.MPet;
import org.springframework.samples.petclinic.dataMigration.mvet.MSpecialty;
import org.springframework.samples.petclinic.dataMigration.mvet.MVet;
import org.springframework.samples.petclinic.dataMigration.mvisit.MVisit;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MigrationServices {

    /**
     * Converts a owner entity to Mowner
     * @param owner
     * @return
     */
    public MOwner convertOwnerToMOwner(Owner owner){
        MOwner mowner = new MOwner();
        mowner.setId(owner.getId().toString());
        mowner.setFirstName(owner.getFirstName());
        mowner.setLastName(owner.getLastName());
        mowner.setAddress(owner.getAddress());
        mowner.setCity(owner.getCity());
        mowner.setTelephone(owner.getTelephone());

        Set<MPet> MPets = new HashSet<>();
        for(Pet pet : owner.getPets()){
            MPet a = convertPetToMPet(pet);
            a.setOwner(mowner);
            MPets.add(convertPetToMPet(pet));

        }

        mowner.setPets(MPets);
        return mowner;
    }

    /**
     * Converts a visit entity to Mvisit
     * @param visit
     * @return
     */
    public MVisit convertVisitToMvisit(Visit visit){
        MVisit mvisit = new MVisit();
        mvisit.setDate(visit.getDate());
        mvisit.setDescription(visit.getDescription());
        mvisit.setPetId(visit.getPetId().toString());
        return mvisit;
    }

    public MVet convertVetToMVet(Vet v) {
        MVet mVet = new MVet();
        mVet.setId(v.getId().toString());
        mVet.setFirstName(v.getFirstName());
        mVet.setLastName(v.getLastName());
        List<Specialty> specialties = v.getSpecialties();
        if (!specialties.isEmpty()) {
            for (Specialty s : specialties) {
                MSpecialty newMSpecialty = new MSpecialty();
                newMSpecialty.setName(s.getName());
                mVet.addSpecialty(newMSpecialty);
            }
        }
        return mVet;
    }

    public MPet convertPetToMPet(Pet pet){
        MPet mpet = new MPet();
        mpet.setBirthDate(pet.getBirthDate());
        mpet.setType(pet.getType());
        mpet.setId(pet.getId().toString());
        mpet.setName(pet.getName());
        return mpet;
    }

    /**
     * Method to prints banners to show that forklifting entity starts/finished
     * @param message
     */
    public void printBanner(String message){

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i<message.length(); i++){
            if(i == 0){
                stringBuilder.append("+");
            }else if(i == message.length()-1){
                stringBuilder.append("+");
            }else{
                stringBuilder.append("-");
            }
        }
        stringBuilder.append("\n"+message+"\n");

        for(int i = 0; i<message.length(); i++){
            if(i == 0){
                stringBuilder.append("+");
            }else if(i == message.length()-1){
                stringBuilder.append("+");
            }else{
                stringBuilder.append("-");
            }
        }

        System.out.println(stringBuilder.toString());
    }

}
