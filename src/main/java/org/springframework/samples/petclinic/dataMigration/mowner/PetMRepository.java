package org.springframework.samples.petclinic.dataMigration.mowner;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.samples.petclinic.owner.Owner;

public interface PetMRepository extends MongoRepository<MPet,String> {
}
