package ftn.tim16.ClinicalCentreSystem.dto.requestandresponse;

import ftn.tim16.ClinicalCentreSystem.model.ExaminationReport;
import ftn.tim16.ClinicalCentreSystem.model.Prescription;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ExaminationReportDTO {
    private Long id;

    @NotEmpty(message = "Comment is empty.")
    private String comment;

    @NotNull(message = "Diagnose is null.")
    private Long diagnoseId;

    private List<Long> medicineIds;

    public ExaminationReportDTO() {
    }

    public ExaminationReportDTO(Long id, String comment, Long diagnoseId, List<Long> medicineIds) {
        this.id = id;
        this.comment = comment;
        this.diagnoseId = diagnoseId;
        this.medicineIds = medicineIds;
    }

    public ExaminationReportDTO(ExaminationReport examinationReport) {
        this.id = examinationReport.getId();
        this.comment = examinationReport.getComment();

        if (examinationReport.getDiagnose() != null) {
            this.diagnoseId = examinationReport.getDiagnose().getId();
        }

        if (examinationReport.getPrescriptions() != null) {
            List<Long> medicines = new ArrayList<>();
            for (Prescription prescription : examinationReport.getPrescriptions()) {
                medicines.add(prescription.getMedicine().getId());
            }
            this.medicineIds = medicines;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getDiagnoseId() {
        return diagnoseId;
    }

    public void setDiagnoseId(Long diagnoseId) {
        this.diagnoseId = diagnoseId;
    }

    public List<Long> getMedicineIds() {
        return medicineIds;
    }

    public void setMedicineIds(List<Long> medicineIds) {
        this.medicineIds = medicineIds;
    }
}
