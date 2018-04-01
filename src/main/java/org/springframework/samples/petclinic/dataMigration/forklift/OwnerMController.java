package org.springframework.samples.petclinic.dataMigration.forklift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ericxiao on 2018-03-31.
 */
@Component
@ConfigurationProperties
@RestController
public class OwnerMController {

    @Autowired
    OwnerMRepository ownerMRepository;

}
