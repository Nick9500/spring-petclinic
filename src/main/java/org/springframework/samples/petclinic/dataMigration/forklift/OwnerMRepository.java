package org.springframework.samples.petclinic.dataMigration.forklift;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.samples.petclinic.owner.Owner;

/**
 * Created by ericxiao on 2018-03-31.
 */
public interface OwnerMRepository extends MongoRepository<Owner,String> {
}
