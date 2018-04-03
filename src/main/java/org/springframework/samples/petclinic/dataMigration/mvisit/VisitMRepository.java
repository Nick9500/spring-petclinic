package org.springframework.samples.petclinic.dataMigration.mvisit;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VisitMRepository extends MongoRepository<MVisit, String> {


}
