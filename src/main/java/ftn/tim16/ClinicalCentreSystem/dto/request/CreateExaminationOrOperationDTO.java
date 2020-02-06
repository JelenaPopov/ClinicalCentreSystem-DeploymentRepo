package ftn.tim16.ClinicalCentreSystem.dto.request;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.DoctorDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ExaminationTypeDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.PatientWithIdDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateExaminationOrOperationDTO {

    @NotEmpty(message = "Kind is empty.")
    private String kind;

    @NotEmpty(message = "Start date and time is empty.")
    private String startDateTime;

    @NotEmpty(message = "End date and time is empty.")
    private String endDateTime;

    @NotNull(message = "Examination type is null")
    private ExaminationTypeDTO examinationType;

    private DoctorDTO doctor;

    @NotNull(message = "Patient  is null")
    private PatientWithIdDTO patient;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public ExaminationTypeDTO getExaminationType() {
        return examinationType;
    }

    public void setExaminationType(ExaminationTypeDTO examinationType) {
        this.examinationType = examinationType;
    }

    public DoctorDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDTO doctor) {
        this.doctor = doctor;
    }

    public PatientWithIdDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientWithIdDTO patient) {
        this.patient = patient;
    }
}
