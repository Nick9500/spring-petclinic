package org.springframework.samples.petclinic.dataMigration;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.model.MBaseEntity;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.MPet;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvet.MVet;
import org.springframework.samples.petclinic.dataMigration.mvet.VetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.MVisit;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@Component
public class ConsistencyChecker {

    private double consistencyPercentage = 100.0;
    private double shadowWriteConsistentPercentage  = 100.0;
    private double shadowReadConsistentPercentage  = 100.0;

    HashFunction hf = Hashing.sha256();
    private static boolean doneForklifting = false;

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


    public HashMap<String, Double> consistencyCheckPercentage = new HashMap<>();
    public double totalCCRows = 0;
    public double consistentCCRows = 0;

    public HashMap<String, Double> shadowWriteCCPercentage = new HashMap<>();
    public double totalShadowWriteRows = 0;
    public double consistentShadowWriteRows = 0;

    public HashMap<String, Double> shadowReadCCPercentage = new HashMap<>();
    public double totalShadowReadRows = 0;
    public double consistentShadowReadRows = 0;

    @Scheduled(cron = "*/60 * * * * *")
    @Async("ConsistencyCheckerThread")
    public void check(){
        if(doneForklifting) {
            System.out.println("Asynchronous consistency Thread: " + Thread.currentThread().getName());
            checkOwners();
            checkVet();
            checkPets();
            checkVisits();
            returnCCPercentage();
        }
    }
    public double returnCCPercentage(){
        double totalRows = consistencyCheckPercentage.get("total rows");
        System.out.println("FINAL Total number of rows is : " + totalRows);
        double consistentRows = consistencyCheckPercentage.get("consistent rows");
        System.out.println("FINAL Total number of consistent rows is : " + consistentRows);

        if(totalRows > 0){
            consistencyPercentage = ( consistentRows / totalRows ) * 100;
        }
        migrationServices.printBanner("Consistency check is at %" + consistencyPercentage);
        //reset consistency check
        totalCCRows = 0;
        consistentCCRows = 0;

        return consistencyPercentage;
    }

    public double returnShadowWritePercentage(){
        double totalRows = shadowWriteCCPercentage.get("total rows");
        System.out.println("SHADOW WRITE Total number of rows is : " + totalRows);
        double consistentRows = shadowWriteCCPercentage.get("consistent rows");
        System.out.println("SHADOW WRITE Total number of consistent rows is : " + consistentRows);
        if(totalRows > 0){
            shadowWriteConsistentPercentage = ( consistentRows / totalRows ) * 100;
        }
        migrationServices.printBanner("SHADOW WRITE Consistency check is at %" + shadowWriteConsistentPercentage);
        //reset consistency check
        totalShadowWriteRows = 0;
        consistentShadowWriteRows = 0;

        return shadowWriteConsistentPercentage;
    }

    public double returnShadowReadPercentage(){
        double totalRows = shadowReadCCPercentage.get("total rows");
        System.out.println("SHADOW READ Total number of rows is : " + totalRows);
        double consistentRows = shadowReadCCPercentage.get("consistent rows");
        System.out.println("SHADOW READ Total number of consistent rows is : " + consistentRows);

        if(totalRows > 0) {
            shadowReadConsistentPercentage = (consistentRows / totalRows) * 100;
        }
        migrationServices.printBanner("SHADOW READ Consistency check is at %" + shadowReadConsistentPercentage);
        //reset consistency check
        totalShadowReadRows = 0;
        consistentShadowReadRows = 0;

        return shadowReadConsistentPercentage;
    }

    public static void enableChecks() {
        doneForklifting = true;
    }

    private void checkOwners(){
        migrationServices.printBanner("Checking for inconsistencies in 'owners'");

        int inconsistencies = 0;

        Map<Integer, Owner> actualCollection = ownerRepository.findAll().stream()
            .collect(Collectors.toMap(owner -> owner.getId(), owner -> owner));

        Map<String, MOwner> expectedCollections = ownerMRepository.findAll().stream()
            .collect(Collectors.toMap(owner -> owner.getId(), owner -> owner));


        for(Integer x : actualCollection.keySet()){
            Owner actual = actualCollection.get(x);
            MOwner migrated = expectedCollections.get(x.toString());
            consistencyCheckPercentage.put("total rows", totalCCRows++);
            if(!compareActualAndExpected(actual, migrated)){
                //inconsistencies
                inconsistencies ++;
                System.out.println("INCONSISTENCY FOUND, INSERTING AGAIN");
                ownerMRepository.save(migrationServices.convertOwnerToMOwner(actual));
            }
            else{
                consistencyCheckPercentage.put("consistent rows", consistentCCRows++);
            }
        }
        migrationServices.printBanner("No. inconsistencies found in owners: " + inconsistencies);
//        return inconsistencies;
        System.out.println("TOTAL NO OF ROWS " + consistencyCheckPercentage.get("total rows"));
        System.out.println("TOTAL CONSIS NO OF ROWS " + consistencyCheckPercentage.get("consistent rows"));
    }


    private void checkVet(){
        migrationServices.printBanner("Checking for inconsistencies in 'vets'");

        int inconsistencies = 0;
        Collection<Vet> vetData = vetRepository.findAll();
        Collection<MVet> mVetData = vetMRepository.findAll();

        ArrayList<Vet> vets = new ArrayList<>(vetData);
        ArrayList<MVet> mVets = new ArrayList<>(mVetData);

        for(int i=0; i<vets.size(); i++){

            Vet original = vets.get(i);
            MVet migrated = mVets.get(i);
            consistencyCheckPercentage.put("total rows", totalCCRows++);
            if(!compareActualAndExpected(original, migrated)){
                inconsistencies++;
                System.out.println("INCONSISTENCY FOUND, INSERTING AGAIN");
                vetMRepository.save(migrationServices.convertVetToMVet(original));
            }
            else{
                consistencyCheckPercentage.put("consistent rows", consistentCCRows++);
            }
        }

        migrationServices.printBanner("No. inconsistencies found in vets: " + inconsistencies);
//        return inconsistencies;
        System.out.println("TOTAL NO OF ROWS " + consistencyCheckPercentage.get("total rows"));
        System.out.println("TOTAL CONSIS NO OF ROWS " + consistencyCheckPercentage.get("consistent rows"));
    }


    private void checkPets() {
        migrationServices.printBanner("Checking for inconsistencies in 'pets'");

        int inconsistencies = 0;
        Map<Integer, Pet> actualCollection = petRepository.findAll().stream()
            .collect(Collectors.toMap(pet -> pet.getId(), pet -> pet));

        Map<String, MPet> expectedCollections = petMRepository.findAll().stream()
            .collect(Collectors.toMap(pet -> pet.getId(), pet -> pet));


        for(Integer x : actualCollection.keySet()){
            Pet actual = actualCollection.get(x);
            MPet migrated = expectedCollections.get(x.toString());
            consistencyCheckPercentage.put("total rows", totalCCRows++);
            if(!compareActualAndExpected(actual, migrated)){
                //inconsistencies
                inconsistencies++;
                System.out.println("INCONSISTENCY FOUND, INSERTING AGAIN");
                MPet newMPet = migrationServices.convertPetToMPet(actual);
                // setting the owner isn't done in convertPetToMPet, so do it outside of method call
                MOwner mOwner = migrationServices.convertOwnerToMOwner( actual.getOwner());
                newMPet.setOwner(mOwner);
                petMRepository.save(newMPet);
            }
            else{
                consistencyCheckPercentage.put("consistent rows", consistentCCRows++);
            }
        }
        migrationServices.printBanner("No. inconsistencies found in owners: " + inconsistencies);
//        return inconsistencies;
        System.out.println("TOTAL NO OF ROWS " + consistencyCheckPercentage.get("total rows"));
        System.out.println("TOTAL CONSIS NO OF ROWS " + consistencyCheckPercentage.get("consistent rows"));
    }

    private void checkVisits() {
        migrationServices.printBanner("Checking for inconsistencies in 'visits'");


        int inconsistencies = 0;
        Collection<Visit> actualCollection = visitRepository.findAll();
        Collection<MVisit> expectedCollection = visitMRepository.findAll();

        ArrayList<Visit> visits = new ArrayList<>(actualCollection);
        ArrayList<MVisit> mVisits = new ArrayList<>(expectedCollection);

        for (int i=0; i<visits.size(); i++){

            Visit actual = visits.get(i);
            MVisit migrated = mVisits.get(i);
            consistencyCheckPercentage.put("total rows", totalCCRows++);
            if(!compareActualAndExpected(actual, migrated)){
                inconsistencies++;
                System.out.println("INCONSISTENCY FOUND, INSERTING AGAIN");
                visitMRepository.save(migrationServices.convertVisitToMvisit(actual));
            }
            else{
                consistencyCheckPercentage.put("consistent rows", consistentCCRows++);
            }
        }

        migrationServices.printBanner("No. inconsistencies found in Visits: " + inconsistencies);
//        return inconsistencies;
        System.out.println("TOTAL NO OF ROWS " + consistencyCheckPercentage.get("total rows"));
        System.out.println("TOTAL CONSIS NO OF ROWS " + consistencyCheckPercentage.get("consistent rows"));
    }

    public boolean compareActualAndExpected(BaseEntity a, MBaseEntity m){
        System.out.println("actual:" + a.toString());
        System.out.println("migrated:" + m.toString());

        HashCode codeOriginal = hf.newHasher()
            .putString(a.toString(), Charsets.UTF_8)
            .hash();

        HashCode codeMigrated = hf.newHasher()
            .putString(m.toString(), Charsets.UTF_8)
            .hash();

        Boolean consistent = codeOriginal.equals(codeMigrated);
        System.out.println("Consistent? -> "+consistent);
        System.out.println("\n");

        return consistent;
    }

    public boolean shadowWriteConsistencyCheck(Owner owner){
        Owner actual = ownerRepository.findById(owner.getId());
        MOwner expected = ownerMRepository.findById(owner.getId().toString()).get();

        shadowWriteCCPercentage.put("total rows", totalShadowWriteRows++);
        if(compareActualAndExpected(actual, expected)){
            shadowWriteCCPercentage.put("consistent rows", consistentShadowWriteRows++);
        }
        returnShadowWritePercentage();
        return compareActualAndExpected(actual, expected);
    }

    public boolean shadowWriteConsistencyCheck(Pet pet){
        Pet actual = petRepository.findById(pet.getId());
        MPet expected = petMRepository.findById(pet.getId().toString()).get();

        shadowWriteCCPercentage.put("total rows", totalShadowWriteRows++);
        if(compareActualAndExpected(actual, expected)){
            shadowWriteCCPercentage.put("consistent rows", consistentShadowWriteRows++);
        }
        returnShadowWritePercentage();
        return compareActualAndExpected(actual, expected);
    }

    public boolean shadowWriteConsistencyCheck(Visit visit){
        Visit actual = visitRepository.findById(visit.getId());
        MVisit expected = visitMRepository.findById(visit.getId().toString()).get();

        shadowWriteCCPercentage.put("total rows", totalShadowWriteRows++);
        if(compareActualAndExpected(actual, expected)){
            shadowWriteCCPercentage.put("consistent rows", consistentShadowWriteRows++);
        }
        returnShadowWritePercentage();
        return compareActualAndExpected(actual, expected);
    }


    public int shadowReadConsistencyCheck(Visit original,  MVisit migrated){
        migrationServices.printBanner("Shadow Read consistency checking for vet's findAll()");
        int inconsistencies = 0;
        shadowReadCCPercentage.put("total rows", totalShadowReadRows++);

        if (!compareActualAndExpected(original, migrated)){
            inconsistencies++;
            System.out.println("Inconsistency found, insert again");
            visitMRepository.deleteById(migrated.getId());
            visitMRepository.save(migrationServices.convertVisitToMvisit(original));
        }
        else{
            shadowReadCCPercentage.put("consistent rows", consistentShadowReadRows++);
        }

        migrationServices.printBanner("No. inconsistencies found in visits: " + inconsistencies);
        returnShadowReadPercentage();
        return inconsistencies;
    }


    public int shadowReadConsistencyCheck(Collection<Vet> vetData, Collection<MVet> mVetData){
        migrationServices.printBanner("Shadow Read consistency checking for vet's findAll()");

        int inconsistencies = 0;
        ArrayList<Vet> vets = new ArrayList<>(vetData);
        ArrayList<MVet> mVets = new ArrayList<>(mVetData);

        for(int i=0; i<vets.size(); i++){
            shadowReadCCPercentage.put("total rows", totalShadowReadRows++);
            Vet original = vets.get(i);
            MVet migrated = mVets.get(i);
            if(!compareActualAndExpected(original, migrated)){
                inconsistencies++;
                System.out.println("INCONSISTENCY FOUND, INSERTING AGAIN");
                vetMRepository.deleteById(migrated.getId());
                vetMRepository.save(migrationServices.convertVetToMVet(original));
            }
            else{
                shadowReadCCPercentage.put("consistent rows", consistentShadowReadRows++);
            }
        }

        migrationServices.printBanner("No. inconsistencies found in vets: " + inconsistencies);
        returnShadowReadPercentage();
        return inconsistencies;
    }

    public int shadowReadConsistencyCheck(Pet original, MPet migrated) {
    	migrationServices.printBanner("Shadow Read consistency checking for pet's findById()");
    	int inconsistencies = 0;

        shadowReadCCPercentage.put("total rows", totalShadowReadRows++);
    	if(!compareActualAndExpected(original, migrated)) {
    		inconsistencies++;
            System.out.println("INCONSISTENCY FOUND, INSERTING AGAIN");
            petMRepository.deleteById(migrated.getId());
            petMRepository.save(migrationServices.convertPetToMPet(original));
    	}
        else{
            shadowReadCCPercentage.put("consistent rows", consistentShadowReadRows++);
        }
        migrationServices.printBanner("No. inconsistencies found in pets: " + inconsistencies);
        returnShadowReadPercentage();
        return inconsistencies;
    }

    public int shadowReadConsistencyCheck(Owner original, MOwner migrated) {
        migrationServices.printBanner("Shadow Read consistency checking for owner's findById()");
        int inconsistencies = 0;

        shadowReadCCPercentage.put("total rows", totalShadowReadRows++);
        if(!compareActualAndExpected(original, migrated)) {
            inconsistencies++;
            System.out.println("INCONSISTENCY FOUND, INSERTING AGAIN");
            ownerMRepository.deleteById(migrated.getId());
            ownerMRepository.save(migrationServices.convertOwnerToMOwner(original));
        }
        else{
            shadowReadCCPercentage.put("consistent rows", consistentShadowReadRows++);
        }

        migrationServices.printBanner("No. inconsistencies found in owners: " + inconsistencies);
        returnShadowReadPercentage();
        return inconsistencies;
    }

}
