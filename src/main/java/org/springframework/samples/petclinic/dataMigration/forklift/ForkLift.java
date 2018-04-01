package org.springframework.samples.petclinic.dataMigration.forklift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.MVisit;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ForkLift {
    @Autowired
    OwnerMRepository ownerMRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    VisitRepository visitRepository;

    @Autowired
    VisitMRepository visitMRepository;

    public void start(){
        forkLiftOwner();
        forkLiftVisits();
    }

    private void forkLiftOwner() {
        printForkLiftBanners(true,"owners");

        ownerMRepository.deleteAll();

        Collection<Owner> forkliftData = ownerRepository.findAll();

        for (Owner owner : forkliftData){

            //BROKEN AS FK
            MOwner mowner = convertOwnerToMOwner(owner);
            System.out.println(mowner);
            ownerMRepository.save(mowner);

        }
        printForkLiftBanners(false,"owners");

    }

    private void forkLiftVisits() {
        printForkLiftBanners(true,"visits");


        visitMRepository.deleteAll();

        Collection<Visit> forkliftedVisitData = visitRepository.findAll();

        for (Visit visit : forkliftedVisitData) {
            MVisit mvisit = convertVisitToMvisit(visit);
            System.out.println(mvisit);
            visitMRepository.save(mvisit);
        }

        printForkLiftBanners(false,"visits");


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

    /**
     * Method to prints banners to show that forklifting entity starts/finished
     * @param start
     * @param entity
     */
    private void printForkLiftBanners(Boolean start, String entity){
        String a;

        if(start){
            a = "Start forklifting "+"entity";
        }else{
            a = "Finished forklifting "+"entity";
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
