package ftn.tim16.ClinicalCentreSystem.dto.requestandresponse;

import ftn.tim16.ClinicalCentreSystem.model.Clinic;
import ftn.tim16.ClinicalCentreSystem.model.Doctor;
import ftn.tim16.ClinicalCentreSystem.model.ExaminationType;

import javax.validation.constraints.*;


public class EditDoctorDTO {
    @NotNull(message = "Id is null.")
    private Long id;

    @NotEmpty(message = "First name is empty.")
    private String firstName;

    @NotEmpty(message = "Last name is empty.")
    private String lastName;

    @NotNull()
    @NotEmpty(message = "WorkHoursFrom is empty.")
    private String workHoursFrom;

    @NotNull()
    @NotEmpty(message = "WorkHoursTo is empty.")
    private String workHoursTo;

    @NotEmpty(message = "Email is empty.")
    @Email(message = "Email is invalid.")
    private String email;

    @NotNull
    private ExaminationTypeDTO specialized;

    @NotEmpty(message = "Phone number is empty.")
    @Size(min = 9, max = 10)
    @Pattern(regexp = "0[0-9]+")
    private String phoneNumber;

    private ClinicDTO clinicDTO;

    public EditDoctorDTO() {

    }

    public EditDoctorDTO(Long id, String firstName, String lastName, String workHoursFrom, String workHoursTo, String email,
                         ExaminationType specialized, String phoneNumber, Clinic clinicDTO) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.workHoursFrom = workHoursFrom;
        this.workHoursTo = workHoursTo;
        this.email = email;
        if (specialized != null) {
            this.specialized = new ExaminationTypeDTO(specialized);
        } else {
            this.specialized = null;
        }

        this.phoneNumber = phoneNumber;

        if (clinicDTO != null) {
            this.clinicDTO = new ClinicDTO(clinicDTO);
        } else {
            this.clinicDTO = null;
        }

    }

    public EditDoctorDTO(Doctor doctor) {
        this(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getWorkHoursFrom().toString(), doctor.getWorkHoursTo().toString(), doctor.getEmail(),
                doctor.getSpecialized(), doctor.getPhoneNumber(), doctor.getClinic());
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

    public ExaminationTypeDTO getSpecialized() {
        return specialized;
    }

    public void setSpecialized(ExaminationTypeDTO specialized) {
        this.specialized = specialized;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ClinicDTO getClinicDTO() {
        return clinicDTO;
    }

    public void setClinicDTO(ClinicDTO clinicDTO) {
        this.clinicDTO = clinicDTO;
    }
}
