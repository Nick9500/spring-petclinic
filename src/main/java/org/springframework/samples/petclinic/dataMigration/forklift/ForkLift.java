package org.springframework.samples.petclinic.dataMigration.forklift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.MPet;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvet.MSpecialty;
import org.springframework.samples.petclinic.dataMigration.mvet.MVet;
import org.springframework.samples.petclinic.dataMigration.mvet.VetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.MVisit;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class ForkLift {
    @Autowired
    OwnerMRepository ownerMRepository;
    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    VetRepository vetRepository;
    @Autowired
    VetMRepository vetMRepository;

    @Autowired
    PetRepository petRepository;
    @Autowired
    PetMRepository petMRepository;

    @Autowired
    VisitRepository visitRepository;
    @Autowired
    VisitMRepository visitMRepository;

    public void start(){
        forkLiftOwner();
        forkLiftVisits();
        forkliftPets();
        vetForklift();
    }

    private void forkLiftOwner() {
        printForkLiftBanners(true,"owners");

        ownerMRepository.deleteAll();
        //owners
        Collection<Owner> forkliftData = ownerRepository.findAll();

        for (Owner owner : forkliftData){
            MOwner mowner = convertOwnerToMOwner(owner);
            System.out.println(mowner);
            ownerMRepository.save(mowner);
        }
        printForkLiftBanners(false,"owners");

    }

    private void forkLiftVisits() {
        printForkLiftBanners(true,"visits");


        visitMRepository.deleteAll();
        //visits
        Collection<Visit> forkliftVisitData = visitRepository.findAll();

        for (Visit visit : forkliftVisitData) {
            MVisit mvisit = convertVisitToMvisit(visit);
            System.out.println(mvisit);
            visitMRepository.save(mvisit);
        }

        printForkLiftBanners(false,"visits");


    }
    private void forkliftPets() {
        printForkLiftBanners(true,"pets");

        petMRepository.deleteAll();
        //pets
        Collection<Pet> forkliftDataPets = petRepository.findAll();

        for (Pet pet : forkliftDataPets){
            MPet mpet = convertPetToMPet(pet);
            System.out.println(mpet);
            petMRepository.save(mpet);

        }

        printForkLiftBanners(false,"pets");

    }


    private void vetForklift(){
        printForkLiftBanners(true, "Vets");
        vetMRepository.deleteAll();
        Collection<Vet> vetData = vetRepository.findAll();
        for(Vet v: vetData){
            MVet mVet = convertVetToMVet(v);
            vetMRepository.save(mVet);
            System.out.println(mVet);
        }

        Collection<Specialty> specialtyData = new Vet().getSpecialties();

        printForkLiftBanners(false, "Vets");

    }

    /**
     * Converts a owner entity to Mowner
     * @param owner
     * @return
     */
    private MOwner convertOwnerToMOwner(Owner owner){
        MOwner mowner = new MOwner();
        mowner.setId(owner.getId().toString());
        mowner.setFirstName(owner.getFirstName());
        mowner.setLastName(owner.getLastName());
        mowner.setAddress(owner.getAddress());
        mowner.setCity(owner.getCity());
        mowner.setTelephone(owner.getTelephone());
        return mowner;
    }

    /**
     * Converts a visit entity to Mvisit
     * @param visit
     * @return
     */
    private MVisit convertVisitToMvisit(Visit visit){
        MVisit mvisit = new MVisit();
        mvisit.setDate(visit.getDate());
        mvisit.setDescription(visit.getDescription());
        mvisit.setPetId(visit.getPetId().toString());
        return mvisit;
    }

    private MVet convertVetToMVet(Vet v) {
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

    private MPet convertPetToMPet(Pet pet){
        MPet mpet = new MPet();
        mpet.setBirthDate(pet.getBirthDate());
        mpet.setType(pet.getType());
        MOwner mOwner = convertOwnerToMOwner(pet.getOwner());
        mpet.setOwner(mOwner);
        mpet.setId(pet.getId().toString());
        return mpet;
    }

    /**
     * Method to prints banners to show that forklifting entity starts/finished
     * @param start
     * @param entity
     */
    private void printForkLiftBanners(Boolean start, String entity){
        String a;

        if(start){
            a = "Start forklifting "+entity;
        }else{
            a = "Finished forklifting "+entity;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i<a.length(); i++){
            if(i == 0){
                stringBuilder.append("+");
            }else if(i == a.length()-1){
                stringBuilder.append("+");
            }else{
                stringBuilder.append("-");
            }
        }
        stringBuilder.append("\n"+a+"\n");

        for(int i = 0; i<a.length(); i++){
            if(i == 0){
                stringBuilder.append("+");
            }else if(i == a.length()-1){
                stringBuilder.append("+");
            }else{
                stringBuilder.append("-");
            }
        }

        System.out.println(stringBuilder.toString());
    }


}
