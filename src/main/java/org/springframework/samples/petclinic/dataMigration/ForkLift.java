package org.springframework.samples.petclinic.dataMigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.MPet;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
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

@Component
public class ForkLift {
    @Autowired
    private OwnerMRepository ownerMRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private VetRepository vetRepository;
    @Autowired
    private VetMRepository vetMRepository;

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PetMRepository petMRepository;

    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private VisitMRepository visitMRepository;

    @Autowired
    private MigrationServices migrationServices;

    public void start(){
        forkLiftOwner();
        forkLiftVisits();
        forkliftPets();
        vetForklift();
        ConsistencyChecker.enableChecks();
    }

    private void forkLiftOwner() {
        migrationServices.printBanner("Starting to forklift 'owners'");

        ownerMRepository.deleteAll();
        //owners
        Collection<Owner> forkliftData = ownerRepository.findAll();

        for (Owner owner : forkliftData){
            MOwner mowner = migrationServices.convertOwnerToMOwner(owner);
            System.out.println(mowner);
            ownerMRepository.save(mowner);
        }
        migrationServices.printBanner("Finished forklifting 'owners'");

    }

    private void forkLiftVisits() {
        migrationServices.printBanner("Starting to forklift 'visits'");


        visitMRepository.deleteAll();
        //visits
        Collection<Visit> forkliftVisitData = visitRepository.findAll();

        for (Visit visit : forkliftVisitData) {
            MVisit mvisit = migrationServices.convertVisitToMvisit(visit);
            System.out.println(mvisit);
            visitMRepository.save(mvisit);
        }

        migrationServices.printBanner("Finished forklifting 'visits'");


    }
    private void forkliftPets() {
        migrationServices.printBanner("Starting to forklift 'pets'");

        petMRepository.deleteAll();
        //pets
        Collection<Pet> forkliftDataPets = petRepository.findAll();

        for (Pet pet : forkliftDataPets){
            MPet mpet = migrationServices.convertPetToMPet(pet);
            MOwner mOwner = migrationServices.convertOwnerToMOwner(pet.getOwner());
            mpet.setOwner(mOwner);
            System.out.println(mpet);
            petMRepository.save(mpet);

        }

        migrationServices.printBanner("Finished forklifting 'pets'");

    }


    private void vetForklift(){
        migrationServices.printBanner("Starting to forklift 'vet'");
        vetMRepository.deleteAll();
        Collection<Vet> vetData = vetRepository.findAll();
        for(Vet v: vetData){
            MVet mVet = migrationServices.convertVetToMVet(v);
            vetMRepository.save(mVet);
            System.out.println(mVet);
        }

        Collection<Specialty> specialtyData = new Vet().getSpecialties();

        migrationServices.printBanner("Finished forklifting 'vets'");

    }



}
