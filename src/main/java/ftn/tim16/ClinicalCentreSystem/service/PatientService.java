package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.request.AwaitingApprovalPatientDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.PatientDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.PatientWithIdDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.PatientPagingDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.PatientStatus;
import ftn.tim16.ClinicalCentreSystem.model.Authority;
import ftn.tim16.ClinicalCentreSystem.model.Patient;
import org.springframework.data.domain.Pageable;

import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.Set;

public interface PatientService {
    Patient changePassword(String newPassword, Patient user);

    List<AwaitingApprovalPatientDTO> findByStatus(PatientStatus patientStatus);

    PatientWithIdDTO approveRequestToRegister(Long id) throws OptimisticLockException;

    boolean rejectRequestToRegister(Long id, String reason) throws OptimisticLockException;

    PatientWithIdDTO activateAccount(Long id);

    PatientPagingDTO getPatientsForMedicalStaffPaging(Long clinicId, String firstName, String lastName, String healthInsuranceId, Pageable page);

    Patient getLoginPatient();

    PatientWithIdDTO getPatientForMedicalStaff(Long id);

    Patient getPatient(Long id);

    PatientDTO create(PatientDTO patientDTO, Set<Authority> authorities);

    Patient findByEmail(String email);

    Patient findByPhoneNumber(String phoneNumber);

}
