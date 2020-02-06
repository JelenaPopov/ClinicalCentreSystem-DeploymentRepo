package ftn.tim16.ClinicalCentreSystem.dto.requestandresponse;

import ftn.tim16.ClinicalCentreSystem.model.MedicalRecord;

import javax.validation.constraints.NotNull;

public class MedicalRecordDTO {
    @NotNull(message = "Id is null")
    private Long id;

    private Integer height;

    private Integer weight;

    private String bloodType;

    private String allergies;

    public MedicalRecordDTO() {
    }

    public MedicalRecordDTO(Long id, Integer height, Integer weight, String bloodType, String allergies) {
        this.id = id;
        this.height = height;
        this.weight = weight;
        this.bloodType = bloodType;
        this.allergies = allergies;
    }

    public MedicalRecordDTO(MedicalRecord medicalRecord) {
        this(medicalRecord.getId(), medicalRecord.getHeight(), medicalRecord.getWeight(), medicalRecord.getBloodType(), medicalRecord.getAllergies());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
}
