package ftn.tim16.ClinicalCentreSystem.dto.requestandresponse;

import ftn.tim16.ClinicalCentreSystem.model.Patient;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PatientDTO {

    @NotEmpty(message = "Email is empty.")
    @Email(message = "Email is invalid.")
    private String email;

    @NotEmpty(message = "Password is empty.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$")
    private String password;

    @NotEmpty(message = "First name is empty.")
    private String firstName;

    @NotEmpty(message = "Last name is empty.")
    private String lastName;

    @NotEmpty(message = "Phone number is empty.")
    @Size(min = 9, max = 10)
    @Pattern(regexp = "0[0-9]+")
    private String phoneNumber;

    @NotEmpty(message = "Address is empty.")
    private String address;

    @NotEmpty(message = "City is empty.")
    private String city;

    @NotEmpty(message = "Country is empty.")
    private String country;

    @NotEmpty(message = "Health insurance ID is empty.")
    @Size(min = 13, max = 13)
    @Pattern(regexp = "[0-9]+")
    private String healthInsuranceID;

    public PatientDTO(String email, String password, String firstName, String lastName, String phoneNumber,
                      String city, String address, String country, String healthInsuranceID) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.country = country;
        this.healthInsuranceID = healthInsuranceID;
    }

    public PatientDTO() {

    }

    public PatientDTO(Patient patient) {
        this(patient.getEmail(), patient.getPassword(), patient.getFirstName(), patient.getLastName(), patient.getPhoneNumber(),
                patient.getCity(), patient.getAddress(), patient.getCountry(), patient.getHealthInsuranceId());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
