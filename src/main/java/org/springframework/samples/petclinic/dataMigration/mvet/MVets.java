package org.springframework.samples.petclinic.dataMigration.mvet;

import org.springframework.samples.petclinic.vet.Vet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class MVets {
    private List<MVet> vets;
    @XmlElement
    public List<MVet> getVetList() {
        if (vets == null) {
            vets = new ArrayList<>();
        }
        return vets;
    }

    public String toString(){
        return vets.toString();
    }
}
