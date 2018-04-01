package org.springframework.samples.petclinic.dataMigration.mowner;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ericxiao on 2018-03-31.
 */
public interface OwnerMRepository extends MongoRepository<MOwner,String> {
}
