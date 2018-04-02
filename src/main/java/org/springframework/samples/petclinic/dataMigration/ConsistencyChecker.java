package org.springframework.samples.petclinic.dataMigration;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.forklift.ForkLift;
import org.springframework.samples.petclinic.dataMigration.model.MBaseEntity;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvet.MVet;
import org.springframework.samples.petclinic.dataMigration.mvet.VetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ConsistencyChecker {

    private int numberOfInconsistency = 0;

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

    public int check(){
        checkOwners();
        checkVet();

        return 0;
    }

    private int checkOwners(){
        System.out.println("Checking owners");
        int inconsistencies = 0;

        Map<Integer, Owner> actualCollection = ownerRepository.findAll().stream()
            .collect(Collectors.toMap(owner -> owner.getId(), owner -> owner));

        Map<String, MOwner> expectedCollections = ownerMRepository.findAll().stream()
            .collect(Collectors.toMap(owner -> owner.getId(), owner -> owner));


        for(Integer x : actualCollection.keySet()){
            Owner actual = actualCollection.get(x);
            MOwner migrated = expectedCollections.get(x.toString());

            if(!compareActualAndExpected(actual, migrated)){
                //inconsistencies
                inconsistencies ++;
            }
        }


        return inconsistencies;
    }


    private int checkVet(){
        System.out.println("Checking vets");

        int inconsistencies = 0;
        Collection<Vet> vetData = vetRepository.findAll();
        Collection<MVet> mVetData = vetMRepository.findAll();

        ArrayList<Vet> vets = new ArrayList<>(vetData);
        ArrayList<MVet> mVets = new ArrayList<>(mVetData);

        for(int i=0; i<vets.size(); i++){

            Vet original = vets.get(i);
            MVet migrated = mVets.get(i);

            if(!compareActualAndExpected(original, migrated)){
                inconsistencies++;
                System.out.println("INCONSISTENCY FOUND, INSERTING AGAIN");
                //vetMRepository.save()
            }
        }
        return inconsistencies;
    }

    private boolean compareActualAndExpected(BaseEntity a, MBaseEntity m){
        HashFunction hf = Hashing.md5();

        HashCode codeOriginal = hf.newHasher()
            .putString(a.toString(), Charsets.UTF_8)
            .hash();

        HashCode codeMigrated = hf.newHasher()
            .putString(m.toString(), Charsets.UTF_8)
            .hash();

        System.out.println(codeOriginal.toString());
        System.out.println(codeMigrated.toString());
        System.out.println(a.toString());
        System.out.println(m.toString());


        return codeOriginal.equals(codeMigrated);
    }

}
