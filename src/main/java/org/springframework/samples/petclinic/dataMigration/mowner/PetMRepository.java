package org.springframework.samples.petclinic.dataMigration.mowner;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PetMRepository extends MongoRepository<MPet,String> {

    @Query("{$orderby: {_ptype: '$ptype'}}")
    List<PetType> findPetTypes();
}
