package ftn.tim16.ClinicalCentreSystem.dto.response;

import ftn.tim16.ClinicalCentreSystem.model.Examination;

import java.util.ArrayList;
import java.util.List;

public class ExaminationPagingDTO {

    private List<ExaminationDTO> examinationList;
    private Integer numberOfItems;

    public ExaminationPagingDTO(List<Examination> examinationList, Integer numberOfItems) {
        List<ExaminationDTO> examinationDTOS = new ArrayList<>();
        for (Examination examination : examinationList) {
            examinationDTOS.add(new ExaminationDTO(examination));
        }
        this.examinationList = examinationDTOS;
        this.numberOfItems = numberOfItems;
    }

    public List<ExaminationDTO> getExaminationList() {
        return examinationList;
    }

    public void setExaminationList(List<ExaminationDTO> examinationList) {
        this.examinationList = examinationList;
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(Integer numberOfItems) {
        this.numberOfItems = numberOfItems;
    }
}
