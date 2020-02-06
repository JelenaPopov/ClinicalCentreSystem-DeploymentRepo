package ftn.tim16.ClinicalCentreSystem.dto.response;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.PatientWithIdDTO;

import java.util.List;

public class PatientPagingDTO {
    private List<PatientWithIdDTO> patientWithIdDTOS;
    private Integer numberOfItems;

    public PatientPagingDTO(List<PatientWithIdDTO> patientWithIdDTOS, Integer numberOfItems) {
        this.patientWithIdDTOS = patientWithIdDTOS;
        this.numberOfItems = numberOfItems;
    }

    public List<PatientWithIdDTO> getPatientWithIdDTOS() {
        return patientWithIdDTOS;
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public void setPatientWithIdDTOS(List<PatientWithIdDTO> patientWithIdDTOS) {
        this.patientWithIdDTOS = patientWithIdDTOS;
    }

    public void setNumberOfItems(Integer numberOfItems) {
        this.numberOfItems = numberOfItems;
    }
}
