package org.springframework.samples.petclinic.dataMigration.mongoSampleIntegration;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ericxiao on 2018-03-31.
 */

public interface EmployeeRepository extends MongoRepository<Employee, String> {
}
