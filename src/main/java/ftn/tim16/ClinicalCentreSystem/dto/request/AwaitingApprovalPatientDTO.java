package ftn.tim16.ClinicalCentreSystem.dto.request;

import ftn.tim16.ClinicalCentreSystem.enumeration.PatientStatus;
import ftn.tim16.ClinicalCentreSystem.model.Patient;

public class AwaitingApprovalPatientDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private PatientStatus status;

    public AwaitingApprovalPatientDTO() {
    }

    public AwaitingApprovalPatientDTO(Long id, String firstName, String lastName, String email, PatientStatus status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
    }

    public AwaitingApprovalPatientDTO(Patient patient) {
        this(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getEmail(), patient.getStatus());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PatientStatus getStatus() {
        return status;
    }

    public void setStatus(PatientStatus status) {
        this.status = status;
    }
}
