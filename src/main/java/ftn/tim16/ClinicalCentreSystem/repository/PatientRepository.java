package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.PatientStatus;
import ftn.tim16.ClinicalCentreSystem.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByStatus(PatientStatus patientStatus);

    Patient findByEmail(String email);

    Patient findByIdAndStatus(Long id, PatientStatus status);

    Patient findByHealthInsuranceId(String healthInsuranceID);

    Patient findByPhoneNumber(String phoneNumber);

    Page<Patient> findDistinctByExaminationsClinicIdAndStatusAndFirstNameContainsIgnoringCaseAndLastNameContainsIgnoringCaseAndHealthInsuranceIdContainsAndExaminationsStatusIn(
            Long id, PatientStatus status, String firstName, String lastName, String healthInsuranceId, Collection<ExaminationStatus> examinationStatus, Pageable page);

    List<Patient> findDistinctByExaminationsClinicIdAndStatusAndFirstNameContainsIgnoringCaseAndLastNameContainsIgnoringCaseAndHealthInsuranceIdContainsAndExaminationsStatusIn(
            Long id, PatientStatus status, String firstName, String lastName, String healthInsuranceId, Collection<ExaminationStatus> examinationStatus);
}
