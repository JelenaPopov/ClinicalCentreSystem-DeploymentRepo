package ftn.tim16.ClinicalCentreSystem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.DiagnoseDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.DoctorDTO;
import ftn.tim16.ClinicalCentreSystem.model.ExaminationReport;
import ftn.tim16.ClinicalCentreSystem.model.Prescription;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExaminationReportForTableDTO {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime timeCreated;

    private String comment;

    private DiagnoseDTO diagnose;

    private List<PrescriptionDTO> prescriptions;

    private DoctorDTO doctor;

    public ExaminationReportForTableDTO() {
    }

    public ExaminationReportForTableDTO(Long id, LocalDateTime timeCreated, String comment, DiagnoseDTO diagnose,
                                        List<PrescriptionDTO> prescriptions, DoctorDTO doctor) {
        this.id = id;
        this.timeCreated = timeCreated;
        this.comment = comment;
        this.diagnose = diagnose;
        this.prescriptions = prescriptions;
        this.doctor = doctor;
    }

    public ExaminationReportForTableDTO(ExaminationReport examinationReport) {
        this.id = examinationReport.getId();
        this.timeCreated = examinationReport.getTimeCreated();
        this.comment = examinationReport.getComment();
        this.diagnose = new DiagnoseDTO(examinationReport.getDiagnose());
        this.doctor = new DoctorDTO(examinationReport.getDoctor());

        List<PrescriptionDTO> prescriptionDTOS = new ArrayList<>();
        for (Prescription prescription : examinationReport.getPrescriptions()) {
            prescriptionDTOS.add(new PrescriptionDTO(prescription));
        }
        this.prescriptions = prescriptionDTOS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DiagnoseDTO getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(DiagnoseDTO diagnose) {
        this.diagnose = diagnose;
    }

    public List<PrescriptionDTO> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<PrescriptionDTO> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public DoctorDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDTO doctor) {
        this.doctor = doctor;
    }
}
