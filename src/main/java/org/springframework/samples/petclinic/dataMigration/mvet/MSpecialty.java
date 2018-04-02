package org.springframework.samples.petclinic.dataMigration.mvet;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.samples.petclinic.dataMigration.model.MNamedEntity;

import java.io.Serializable;

@Document(collection="specialties")
public class MSpecialty extends MNamedEntity implements Serializable{
}
