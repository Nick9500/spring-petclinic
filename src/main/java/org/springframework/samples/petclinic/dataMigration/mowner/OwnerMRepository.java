package org.springframework.samples.petclinic.dataMigration.mowner;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by ericxiao on 2018-03-31.
 */
public interface OwnerMRepository extends MongoRepository<MOwner,String> {

    @Query("{lastName: {$regex: '^?0.*', $options:'i'}}")
    List<MOwner> findByLastName(String lastName);
}
