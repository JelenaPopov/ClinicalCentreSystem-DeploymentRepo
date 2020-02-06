package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ClinicalCentreAdminDTO;
import ftn.tim16.ClinicalCentreSystem.model.ClinicalCentreAdministrator;

import java.util.List;

public interface ClinicalCentreAdministratorService {
    ClinicalCentreAdministrator changePassword(String newPassword, ClinicalCentreAdministrator user);

    ClinicalCentreAdministrator getLoginAdmin();

    List<ClinicalCentreAdminDTO> getAllClinicalCentreAdministrators(Long clinicalCentreAdminId);

    ClinicalCentreAdminDTO create(ClinicalCentreAdminDTO clinicalCentreAdminDTO);

    ClinicalCentreAdministrator findByEmail(String email);

    ClinicalCentreAdministrator findByPhoneNumber(String phoneNumber);
}
