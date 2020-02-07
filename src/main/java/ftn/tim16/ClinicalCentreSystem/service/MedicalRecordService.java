package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.MedicalRecordDTO;
import ftn.tim16.ClinicalCentreSystem.model.MedicalRecord;
import ftn.tim16.ClinicalCentreSystem.model.Patient;

public interface MedicalRecordService {

    MedicalRecord findByPatientId(Long patientId);

    MedicalRecord create(Patient patient);

    MedicalRecordDTO update(MedicalRecordDTO medicalRecordDTO);

}
