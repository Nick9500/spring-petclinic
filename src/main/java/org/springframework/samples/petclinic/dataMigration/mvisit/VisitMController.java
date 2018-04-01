package org.springframework.samples.petclinic.dataMigration.mvisit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.samples.petclinic.dataMigration.mowner.OwnerMRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Component
@ConfigurationProperties
@RestController
public class VisitMController {

    @Autowired
    VisitMRepository visitMRepository;
}
