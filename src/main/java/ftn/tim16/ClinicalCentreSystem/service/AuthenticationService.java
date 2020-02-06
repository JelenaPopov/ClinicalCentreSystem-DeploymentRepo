package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.PatientDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.LoggedInUserDTO;
import ftn.tim16.ClinicalCentreSystem.model.Authority;
import ftn.tim16.ClinicalCentreSystem.security.auth.JwtAuthenticationRequest;

import java.util.Set;

public interface AuthenticationService {
    PatientDTO registerPatient(PatientDTO patientDTO);

    LoggedInUserDTO login(JwtAuthenticationRequest authenticationRequest);

    Set<Authority> findByName(String name);

    Set<Authority> findById(Long id);
}
