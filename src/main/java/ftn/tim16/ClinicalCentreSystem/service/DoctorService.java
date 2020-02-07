package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateDoctorDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.DoctorDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.EditDoctorDTO;
import ftn.tim16.ClinicalCentreSystem.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface DoctorService {
    DoctorDTO create(CreateDoctorDTO doctor, ClinicAdministrator clinicAdministrator);

    List<DoctorDTO> findAllDoctorsInClinic(Clinic clinic);

    List<DoctorDTO> searchDoctorsInClinic(Clinic clinic, String firstName, String lastName, String specializedFor);

    Doctor changePassword(String newPassword, Doctor user);

    boolean isAvailable(Doctor doctor, LocalDateTime startDateTime, LocalDateTime endDateTime);

    boolean canGetTimeOff(Doctor doctor, LocalDateTime startDateTime, LocalDateTime endDateTime);

    Doctor getAvailableDoctor(ExaminationType specialized, LocalDateTime startDateTime, LocalDateTime endDateTime, Long clinicId);

    Set<Doctor> getAvailableDoctors(ExaminationType specialized, LocalDateTime startDateTime, LocalDateTime endDateTime, Long clinicId);

    void removeExamination(Examination examination, String email);

    Doctor getLoginDoctor();

    List<DoctorDTO> getAllAvailableDoctors(Long specializedId, Long clinicId, String startDateTime, String endDateTime);

    Doctor getDoctor(Long id);

    Doctor findById(Long id);

    DoctorDTO deleteDoctor(Long clinicId, Long id);

    List<Doctor> findDoctorsByClinicIdAndExaminationTypeId(Long clinicId, Long specializedId);

    DoctorDTO editPersonalInformation(EditDoctorDTO editDoctorDTO);

    EditDoctorDTO findDoctorById(Long id);

    Doctor findByEmail(String email);

    Doctor findByPhoneNumber(String phoneNumber);

    boolean haveToChangeDoctor(Examination assignedExamination, Doctor doctor, LocalDateTime startDateTime, LocalDateTime endDateTime);

}
