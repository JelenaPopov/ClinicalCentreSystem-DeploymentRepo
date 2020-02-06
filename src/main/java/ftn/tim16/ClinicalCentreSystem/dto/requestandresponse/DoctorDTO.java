package ftn.tim16.ClinicalCentreSystem.dto.requestandresponse;

import ftn.tim16.ClinicalCentreSystem.model.Doctor;
import ftn.tim16.ClinicalCentreSystem.model.ExaminationType;

public class DoctorDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String workHoursFrom;

    private String workHoursTo;

    private String email;

    private String password;

    private ExaminationTypeDTO specialized;

    private String phoneNumber;

    private Double doctorRating;

    public DoctorDTO() {

    }

    public DoctorDTO(Long id, String phoneNumber, String firstName, String lastName, String workHoursFrom, String workHoursTo, String email, String password,
                     ExaminationType specialized, Double doctorRating) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.workHoursFrom = workHoursFrom;
        this.workHoursTo = workHoursTo;
        this.email = email;
        this.password = password;
        if (specialized != null) {
            this.specialized = new ExaminationTypeDTO(specialized);
        } else {
            this.specialized = null;
        }
        this.doctorRating = doctorRating;
    }

    public DoctorDTO(Doctor doctor) {
        this(doctor.getId(), doctor.getPhoneNumber(), doctor.getFirstName(), doctor.getLastName(), doctor.getWorkHoursFrom().toString(), doctor.getWorkHoursTo().toString(), doctor.getEmail(),
                doctor.getPassword(), doctor.getSpecialized(), doctor.getDoctorRating() == null ? 0 : doctor.getDoctorRating());
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ExaminationTypeDTO getSpecialized() {
        return specialized;
    }

    public void setSpecialized(ExaminationTypeDTO specialized) {
        this.specialized = specialized;
    }

    public Double getDoctorRating() {
        return doctorRating;
    }

    public void setDoctorRating(Double doctorRating) {
        this.doctorRating = doctorRating;
    }


}
