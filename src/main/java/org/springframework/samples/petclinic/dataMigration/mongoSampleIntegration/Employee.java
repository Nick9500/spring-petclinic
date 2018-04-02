package org.springframework.samples.petclinic.dataMigration.mongoSampleIntegration;

/**
 * Created by ericxiao on 2018-03-31.
 */
//@Document(collection ="employees")
public class Employee extends Personx{

    private String firstName;

    public Employee(String firstName, String lastName) {
        super(lastName);
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
