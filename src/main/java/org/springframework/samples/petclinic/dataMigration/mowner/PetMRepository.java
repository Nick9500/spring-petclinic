package org.springframework.samples.petclinic.dataMigration.mowner;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PetMRepository extends MongoRepository<MPet,String> {

}
