package org.springframework.samples.petclinic.dataMigration;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.forklift.ForkLift;
import org.springframework.samples.petclinic.dataMigration.mowner.MOwner;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.samples.petclinic.dataMigration.mowner.PetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvet.MVet;
import org.springframework.samples.petclinic.dataMigration.mvet.VetMRepository;
import org.springframework.samples.petclinic.dataMigration.mvisit.VisitMRepository;
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

    private HashFunction hf;

    public int check(){
        hf = Hashing.md5();
        checkOwners();
        checkVet();
        return 0;
    }

    private int checkOwners(){

        Map<Integer, Owner> actualCollection = ownerRepository.findAll().stream()
            .collect(Collectors.toMap(owner -> owner.getId(), owner -> owner));

        Map<String, MOwner> expectedCollections = ownerMRepository.findAll().stream()
            .collect(Collectors.toMap(owner -> owner.getId(), owner -> owner));


        return 0;
    }

    private int checkVet(){
        Collection<Vet> vetData = vetRepository.findAll();
        Collection<MVet> mVetData = vetMRepository.findAll();

        ArrayList<Vet> vets = new ArrayList<>(vetData);
        ArrayList<MVet> mVets = new ArrayList<>(mVetData);

        for(int i=0; i<vets.size(); i++){
            //MVet original = ForkLift.convertVetToMVet(vets.get(i));
            Vet original = vets.get(i);
            MVet migrated = mVets.get(i);

            HashCode codeOriginal = hf.newHasher()
                .putString(original.toString(), Charsets.UTF_8)
                .hash();
            HashCode codeMigrated = hf.newHasher()
                .putString(migrated.toString(), Charsets.UTF_8)
                .hash();

            System.out.println("HASH CODES");
            System.out.println(original.toString());
            System.out.println(migrated.toString());
            System.out.println(codeOriginal.toString());
            System.out.println(codeMigrated.toString());
            if(codeOriginal.equals(codeMigrated.toString())){
                System.out.println("INCONSISTENCY FOUND, INSERTING OBJECT AGAIN");
                System.out.println(original.toString());
                System.out.println(migrated.toString());
            }


        }
        return 0;
    }

}
