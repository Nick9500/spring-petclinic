package org.springframework.samples.petclinic.dataMigration.consistencyChecking;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dataMigration.forklift.ForkLift;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class ConsistencyChecking {
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

    public void checkVet(Collection<Vet> vetData, Collection<MVet> mVetData){
//        Collection<Vet> vetData = vetRepository.findAll();
//        Collection<MVet> mVetData = vetMRepository.findAll();

        ArrayList<Vet> vets = new ArrayList<>(vetData);
        ArrayList<MVet> mVets = new ArrayList<>(mVetData);

        HashFunction hf = Hashing.md5();

        for(int i=0; i<vets.size(); i++){
            MVet original = ForkLift.convertVetToMVet(vets.get(i));
            MVet migrated = mVets.get(i);

            HashCode codeOriginal = hf.newHasher()
                .putString(original.toString(), Charsets.UTF_8)
                .hash();
            HashCode codeMigrated = hf.newHasher()
                .putString(migrated.toString(), Charsets.UTF_8)
                .hash();

            System.out.println("HASH CODES");
            System.out.println(codeOriginal.toString());
            System.out.println(codeMigrated.toString());
        }
    }
}
