package org.springframework.samples.petclinic.dataMigration.mongoSampleIntegration;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by ericxiao on 2018-03-31.
 */
@Document(collection = "employees")
public class Employee {
    @Id
    private String id;

    private String fullName;

    public Employee(String fullName) {
        this.fullName = fullName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
