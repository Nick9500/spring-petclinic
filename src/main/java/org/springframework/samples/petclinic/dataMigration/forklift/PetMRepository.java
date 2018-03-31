package org.springframework.samples.petclinic.dataMigration.forklift;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.samples.petclinic.owner.Owner;

public interface PetMRepository extends MongoRepository<Owner,String> {
}
