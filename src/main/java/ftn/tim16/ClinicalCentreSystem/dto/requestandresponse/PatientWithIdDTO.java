package ftn.tim16.ClinicalCentreSystem.dto.requestandresponse;

import ftn.tim16.ClinicalCentreSystem.enumeration.PatientStatus;
import ftn.tim16.ClinicalCentreSystem.model.Patient;

public class PatientWithIdDTO {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address;

    private String city;

    private String country;

    private String healthInsuranceID;

    private PatientStatus status;

    public PatientWithIdDTO(Long id, String email, String firstName, String lastName, String phoneNumber, String address,
                            String city, String country, String healthInsuranceID, PatientStatus status) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.country = country;
        this.healthInsuranceID = healthInsuranceID;
        this.status = status;
    }

    public PatientWithIdDTO(Patient patient) {
        this(patient.getId(), patient.getEmail(), patient.getFirstName(), patient.getLastName(), patient.getPhoneNumber(),
                patient.getAddress(), patient.getCity(), patient.getCountry(), patient.getHealthInsuranceId(), patient.getStatus());
    }

    public PatientWithIdDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHealthInsuranceID() {
        return healthInsuranceID;
    }

    public void setHealthInsuranceID(String healthInsuranceID) {
        this.healthInsuranceID = healthInsuranceID;
    }

    public PatientStatus getStatus() {
        return status;
    }

    public void setStatus(PatientStatus status) {
        this.status = status;
    }
}
