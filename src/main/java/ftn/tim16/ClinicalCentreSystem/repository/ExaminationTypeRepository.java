package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.enumeration.LogicalStatus;
import ftn.tim16.ClinicalCentreSystem.model.ExaminationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExaminationTypeRepository extends JpaRepository<ExaminationType, Long> {
    ExaminationType findByLabelIgnoringCase(String label);

    List<ExaminationType> findByClinicIdAndStatus(Long id, LogicalStatus status);

    List<ExaminationType> findByClinicIdAndStatusAndLabelContainsIgnoringCaseAndPrice(Long id, LogicalStatus status, String label, Double price);

    List<ExaminationType> findByClinicIdAndStatusAndLabelContainsIgnoringCase(Long id, LogicalStatus status, String label);

    ExaminationType findByIdAndStatusNot(Long id, LogicalStatus status);
}
