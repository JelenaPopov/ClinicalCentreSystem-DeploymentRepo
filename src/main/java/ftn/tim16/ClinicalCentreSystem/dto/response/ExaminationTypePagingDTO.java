package ftn.tim16.ClinicalCentreSystem.dto.response;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ExaminationTypeDTO;

import java.util.List;

public class ExaminationTypePagingDTO {
    private List<ExaminationTypeDTO> examinationTypes;
    private Integer numberOfItems;

    public ExaminationTypePagingDTO(List<ExaminationTypeDTO> examinationTypes, Integer numberOfItems) {
        this.examinationTypes = examinationTypes;
        this.numberOfItems = numberOfItems;
    }

    public List<ExaminationTypeDTO> getExaminationTypes() {
        return examinationTypes;
    }

    public void setExaminationTypes(List<ExaminationTypeDTO> examinationTypes) {
        this.examinationTypes = examinationTypes;
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(Integer numberOfItems) {
        this.numberOfItems = numberOfItems;
    }
}
