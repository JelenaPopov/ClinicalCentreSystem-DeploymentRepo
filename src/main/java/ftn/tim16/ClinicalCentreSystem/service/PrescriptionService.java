package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.response.PrescriptionDTO;

import java.util.List;

public interface PrescriptionService {

    List<PrescriptionDTO> getUnstampedPrescriptions(Long nurseId);

    PrescriptionDTO stampPrescription(Long prescriptionId, Long nurseId);
}
