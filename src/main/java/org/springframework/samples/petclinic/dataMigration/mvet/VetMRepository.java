package org.springframework.samples.petclinic.dataMigration.mvet;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VetMRepository extends MongoRepository<MVet,String> {

}
