package ftn.tim16.ClinicalCentreSystem.dto.request;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ExaminationTypeDTO;

import javax.validation.constraints.*;

public class CreateDoctorDTO {

    @NotEmpty(message = "First name is empty.")
    private String firstName;

    @NotEmpty(message = "Last name is empty.")
    private String lastName;

    @NotNull()
    private String workHoursFrom;

    @NotNull()
    private String workHoursTo;

    @NotEmpty(message = "Email is empty.")
    @Email(message = "Email is invalid.")
    private String email;

    @NotEmpty(message = "Phone number is empty.")
    @Size(min = 9, max = 10)
    @Pattern(regexp = "0[0-9]+")
    private String phoneNumber;

    @NotNull
    private ExaminationTypeDTO specialized;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getWorkHoursFrom() {
        return workHoursFrom;
    }

    public void setWorkHoursFrom(String workHoursFrom) {
        this.workHoursFrom = workHoursFrom;
    }

    public String getWorkHoursTo() {
        return workHoursTo;
    }

    public void setWorkHoursTo(String workHoursTo) {
        this.workHoursTo = workHoursTo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ExaminationTypeDTO getSpecialized() {
        return specialized;
    }

    public void setSpecialized(ExaminationTypeDTO specialized) {
        this.specialized = specialized;
    }
}
