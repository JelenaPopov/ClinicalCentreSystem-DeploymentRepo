package ftn.tim16.ClinicalCentreSystem.dto.requestandresponse;

import ftn.tim16.ClinicalCentreSystem.model.Clinic;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ClinicDTO {
    private Long id;

    @NotEmpty(message = "Name is empty.")
    @Size(message = "Max size for name is 50.", max = 50)
    private String name;

    @NotEmpty(message = "Description is empty.")
    private String description;

    @NotEmpty(message = "Address is empty.")
    private String address;

    private Double clinicRating;

    public ClinicDTO() {
    }

    public ClinicDTO(Long id, String name, String description, String address) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
    }

    public ClinicDTO(Long id, String name, String description, String address, Double clinicRating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.clinicRating = clinicRating;
    }

    public ClinicDTO(Clinic clinic) {
        this(clinic.getId(), clinic.getName(), clinic.getDescription(), clinic.getAddress(), clinic.getClinicRating());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getClinicRating() {
        return clinicRating;
    }

    public void setClinicRating(Double clinicRating) {
        this.clinicRating = clinicRating;
    }
}
