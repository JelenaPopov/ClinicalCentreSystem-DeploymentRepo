package ftn.tim16.ClinicalCentreSystem.dto.response;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.NurseDTO;

import java.util.List;

public class NursesPagingDTO {
    private List<NurseDTO> nurses;
    private Integer numberOfItems;

    public NursesPagingDTO(List<NurseDTO> nurses, Integer numberOfItems) {
        this.nurses = nurses;
        this.numberOfItems = numberOfItems;
    }

    public List<NurseDTO> getNurses() {
        return nurses;
    }

    public void setNurses(List<NurseDTO> nurses) {
        this.nurses = nurses;
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(Integer numberOfItems) {
        this.numberOfItems = numberOfItems;
    }
}
