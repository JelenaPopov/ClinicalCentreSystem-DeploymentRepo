package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.EditNurseDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.NurseDTO;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;
import ftn.tim16.ClinicalCentreSystem.model.Nurse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface NurseService {

    Nurse changePassword(String newPassword, Nurse user);

    Nurse findById(Long id);

    List<NurseDTO> getAllNursesInClinic(Long id);

    List<NurseDTO> getAllNursesInClinic(Long id, Pageable page);

    NurseDTO create(NurseDTO nurseDTO, ClinicAdministrator clinicAdministrator);

    boolean canGetTimeOff(Nurse nurse, LocalDateTime startDateTime, LocalDateTime endDateTime);

    Nurse getRandomNurse(Long clinic_id, LocalDateTime startDateTime, LocalDateTime endDateTime);

    Nurse getLoginNurse();

    NurseDTO editPersonalInformation(EditNurseDTO editNurseDTO);

    EditNurseDTO findNurseById(Long id);

    Nurse findByEmail(String email);

    Nurse findByPhoneNumber(String phoneNumber);
}
