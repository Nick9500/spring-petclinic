package org.springframework.samples.petclinic.mongoSampleIntegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ericxiao on 2018-03-31.
 */
@Component
@ConfigurationProperties
@RestController
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @RequestMapping("/test")
    String home(){
        return "Hello World...- TO MONGO DB <form action='/employee' method='POST'><input type='text' name='fullname'/><button type='submit'>SUBMIT DIS SHIT</button></form>";
    }

    @RequestMapping(value ="/employee", method = RequestMethod.POST)
    public Employee create(@RequestBody String name){
        Employee a = new Employee(name);
        return employeeRepository.save(a);
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public Employee get(@PathVariable String id){
//        return employeeRepository.findOne(id);
//    }
}
