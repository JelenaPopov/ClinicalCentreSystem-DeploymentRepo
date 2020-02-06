package ftn.tim16.ClinicalCentreSystem.dto.response;

import ftn.tim16.ClinicalCentreSystem.model.Doctor;
import ftn.tim16.ClinicalCentreSystem.model.Patient;
import ftn.tim16.ClinicalCentreSystem.model.Prescription;

public class PrescriptionDTO {
    private Long id;

    private String medicine;

    private String doctor;

    private String patient;

    public PrescriptionDTO() {
    }

    public PrescriptionDTO(Long id, String medicine, String doctor, String patient) {
        this.id = id;
        this.medicine = medicine;
        this.doctor = doctor;
        this.patient = patient;
    }

    public PrescriptionDTO(Prescription prescription) {
        Doctor doctor = prescription.getExaminationReport().getDoctor();
        Patient patient = prescription.getExaminationReport().getExamination().getPatient();
        this.id = prescription.getId();
        this.medicine = prescription.getMedicine().getLabel();
        this.doctor = doctor.getFirstName() + " " + doctor.getLastName();
        this.patient = patient.getFirstName() + " " + patient.getLastName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }
}
