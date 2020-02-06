package ftn.tim16.ClinicalCentreSystem.dto.response;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.*;
import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationStatus;
import ftn.tim16.ClinicalCentreSystem.model.DateTimeInterval;
import ftn.tim16.ClinicalCentreSystem.model.Doctor;
import ftn.tim16.ClinicalCentreSystem.model.Examination;

import java.util.ArrayList;
import java.util.List;

public class ExaminationDTO {

    private Long id;

    private String kind;

    private DateTimeInterval interval;

    private RoomWithIdDTO room;

    private PatientWithIdDTO patient;

    private ExaminationStatus status;

    private ExaminationTypeDTO examinationType;

    private List<DoctorDTO> doctors;

    private Integer discount;

    private NurseDTO nurse;

    public ExaminationDTO() {

    }

    public ExaminationDTO(Long id, String kind, DateTimeInterval interval, RoomWithIdDTO room, PatientWithIdDTO patient,
                          ExaminationStatus status, ExaminationTypeDTO examinationType, List<DoctorDTO> doctors, Integer discount, NurseDTO nurse) {
        this.id = id;
        this.kind = kind;
        this.interval = interval;
        this.room = room;
        this.patient = patient;
        this.status = status;
        this.examinationType = examinationType;
        this.doctors = doctors;
        this.discount = discount;
        this.nurse = nurse;
    }

    public ExaminationDTO(Examination examination) {
        this.id = examination.getId();
        this.kind = examination.getKind().toString();
        this.interval = examination.getInterval();

        if (examination.getRoom() != null) {
            this.room = new RoomWithIdDTO(examination.getRoom());
        } else {
            this.room = null;
        }
        if (examination.getPatient() != null) {
            this.patient = new PatientWithIdDTO(examination.getPatient());
        } else {
            this.patient = null;
        }

        this.status = examination.getStatus();
        if (examination.getExaminationType() != null) {
            this.examinationType = new ExaminationTypeDTO(examination.getExaminationType());
        } else {
            this.examinationType = null;
        }

        this.doctors = listDoctorsToDTO(examination);
        this.discount = examination.getDiscount();
        if (examination.getNurse() != null) {
            this.nurse = new NurseDTO(examination.getNurse());
        } else {
            this.nurse = null;
        }

    }

    private List<DoctorDTO> listDoctorsToDTO(Examination examination) {
        if (examination.getDoctors() == null) {
            return new ArrayList<>();
        }
        List<DoctorDTO> doctorDTOS = new ArrayList<>();
        for (Doctor doctor : examination.getDoctors()) {
            doctorDTOS.add(new DoctorDTO(doctor));
        }
        return doctorDTOS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public DateTimeInterval getInterval() {
        return interval;
    }

    public void setInterval(DateTimeInterval interval) {
        this.interval = interval;
    }

    public RoomWithIdDTO getRoom() {
        return room;
    }

    public void setRoom(RoomWithIdDTO room) {
        this.room = room;
    }

    public PatientWithIdDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientWithIdDTO patient) {
        this.patient = patient;
    }

    public ExaminationStatus getStatus() {
        return status;
    }

    public void setStatus(ExaminationStatus status) {
        this.status = status;
    }

    public ExaminationTypeDTO getExaminationType() {
        return examinationType;
    }

    public void setExaminationType(ExaminationTypeDTO examinationType) {
        this.examinationType = examinationType;
    }

    public List<DoctorDTO> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DoctorDTO> doctors) {
        this.doctors = doctors;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public NurseDTO getNurse() {
        return nurse;
    }

    public void setNurse(NurseDTO nurse) {
        this.nurse = nurse;
    }
}
