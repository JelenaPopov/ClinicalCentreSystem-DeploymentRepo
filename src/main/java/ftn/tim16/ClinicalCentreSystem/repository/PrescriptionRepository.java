package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.enumeration.PrescriptionStatus;
import ftn.tim16.ClinicalCentreSystem.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByNurseIdAndStatus(Long nurseId, PrescriptionStatus status);

    Prescription findByIdAndNurseIdAndStatus(Long prescriptionId, Long nurseId, PrescriptionStatus status);
}
