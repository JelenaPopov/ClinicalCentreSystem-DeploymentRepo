package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.model.ExaminationReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExaminationReportRepository extends JpaRepository<ExaminationReport, Long> {

    ExaminationReport findByExaminationId(Long examinationId);

    List<ExaminationReport> findByMedicalRecordPatientIdOrderByTimeCreatedDesc(Long patientId);

    ExaminationReport findByIdAndDoctorId(Long id, Long doctorId);

}
