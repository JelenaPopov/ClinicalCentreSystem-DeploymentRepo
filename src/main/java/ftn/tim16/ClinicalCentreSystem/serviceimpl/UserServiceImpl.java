package ftn.tim16.ClinicalCentreSystem.serviceimpl;

import ftn.tim16.ClinicalCentreSystem.dto.request.UserDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.DoctorStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.UserStatus;
import ftn.tim16.ClinicalCentreSystem.model.*;
import ftn.tim16.ClinicalCentreSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private ClinicAdministratorService clinicAdministratorService;

    @Autowired
    private ClinicalCentreAdministratorService clinicalCentreAdministratorService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = findUserByEmail(email);
        if (userDetails != null) {
            return userDetails;
        }
        throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public UserDetails changePassword(UserDTO userDTO) {
        UserDetails user = findUserByEmail(userDTO.getEmail());

        if (user == null) {
            return null;
        }

        if (!passwordEncoder.matches(userDTO.getOldPassword(), user.getPassword())) {
            return null;
        }

        String newPassword = passwordEncoder.encode(userDTO.getNewPassword());
        if (user instanceof ClinicAdministrator) {
            return clinicAdministratorService.changePassword(newPassword, (ClinicAdministrator) user);
        } else if (user instanceof ClinicalCentreAdministrator) {
            return clinicalCentreAdministratorService.changePassword(newPassword, (ClinicalCentreAdministrator) user);
        } else if (user instanceof Doctor) {
            return doctorService.changePassword(newPassword, (Doctor) user);
        } else if (user instanceof Patient) {
            return patientService.changePassword(newPassword, (Patient) user);
        } else if (user instanceof Nurse) {
            return nurseService.changePassword(newPassword, (Nurse) user);
        }
        return null;
    }

    @Override
    public UserDetails findUserByEmail(String email) {
        ClinicalCentreAdministrator clinicalCentreAdministrator = clinicalCentreAdministratorService.findByEmail(email);
        if (clinicalCentreAdministrator != null) {
            return clinicalCentreAdministrator;
        }

        ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByEmail(email);
        if (clinicAdministrator != null) {
            return clinicAdministrator;
        }

        Patient patient = patientService.findByEmail(email);
        if (patient != null) {
            return patient;
        }

        Nurse nurse = nurseService.findByEmail(email);
        if (nurse != null) {
            return nurse;
        }

        Doctor doctor = doctorService.findByEmail(email);
        if (doctor != null) {
            return doctor;
        }

        return null;
    }

    @Override
    public boolean neverLoggedIn(String email) {
        ClinicalCentreAdministrator clinicalCentreAdministrator = clinicalCentreAdministratorService.findByEmail(email);
        if (clinicalCentreAdministrator != null) {
            return (clinicalCentreAdministrator.getStatus() == UserStatus.NEVER_LOGGED_IN);
        }

        ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByEmail(email);
        if (clinicAdministrator != null) {
            return (clinicAdministrator.getStatus() == UserStatus.NEVER_LOGGED_IN);
        }

        Nurse nurse = nurseService.findByEmail(email);
        if (nurse != null) {
            return (nurse.getStatus() == UserStatus.NEVER_LOGGED_IN);
        }

        Doctor doctor = doctorService.findByEmail(email);
        if (doctor != null) {
            return (doctor.getStatus() == DoctorStatus.NEVER_LOGGED_IN);
        }

        return false;
    }

}
