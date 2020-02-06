package ftn.tim16.ClinicalCentreSystem.dto.request;

import ftn.tim16.ClinicalCentreSystem.model.ExaminationType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CreateExaminationTypeDTO {

    private Long id;

    @NotEmpty(message = "Label is empty.")
    @Size(message = "Max size for label is 30.", max = 30)
    private String label;

    @NotNull(message = "Price is null.")
    @Positive(message = "Price is not a positive number.")
    private Double price;

    public CreateExaminationTypeDTO(Long id, String label, Double price) {
        super();
        this.id = id;
        this.label = label;
        this.price = price;
    }

    public CreateExaminationTypeDTO(ExaminationType examinationType) {
        this(examinationType.getId(), examinationType.getLabel(), examinationType.getPrice());
    }

    public CreateExaminationTypeDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
