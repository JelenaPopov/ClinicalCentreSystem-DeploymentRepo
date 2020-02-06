package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ClinicAdministratorDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.EditClinicAdminDTO;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;

import java.util.List;

public interface ClinicAdministratorService {

    ClinicAdministrator changePassword(String newPassword, ClinicAdministrator user);

    ClinicAdministrator getLoginAdmin();

    ClinicAdministratorDTO editPersonalInformation(EditClinicAdminDTO editClinicAdminDTO);

    EditClinicAdminDTO findClinicAdminById(Long id);

    List<ClinicAdministratorDTO> getAllClinicAdministratorsInClinic(Long id);

    ClinicAdministratorDTO create(ClinicAdministratorDTO clinicAdministratorDTO);

    ClinicAdministrator findRandomAdminInClinic(Long clinicId);

    ClinicAdministrator findByEmail(String email);

    ClinicAdministrator findByPhoneNumber(String phoneNumber);
}
