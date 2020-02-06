package ftn.tim16.ClinicalCentreSystem.serviceimpl;

import ftn.tim16.ClinicalCentreSystem.common.RandomPasswordGenerator;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ClinicAdministratorDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.EditClinicAdminDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.UserStatus;
import ftn.tim16.ClinicalCentreSystem.model.Authority;
import ftn.tim16.ClinicalCentreSystem.model.Clinic;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;
import ftn.tim16.ClinicalCentreSystem.repository.ClinicAdministratorRepository;
import ftn.tim16.ClinicalCentreSystem.service.AuthenticationService;
import ftn.tim16.ClinicalCentreSystem.service.ClinicAdministratorService;
import ftn.tim16.ClinicalCentreSystem.service.ClinicService;
import ftn.tim16.ClinicalCentreSystem.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ClinicAdministratorServiceImpl implements ClinicAdministratorService {

    @Autowired
    private ClinicAdministratorRepository clinicAdministratorRepository;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Override
    @Transactional(readOnly = false)
    public ClinicAdministrator changePassword(String newPassword, ClinicAdministrator user) {
        user.setPassword(newPassword);
        if (user.getStatus().equals(UserStatus.NEVER_LOGGED_IN)) {
            user.setStatus(UserStatus.ACTIVE);
        }
        return clinicAdministratorRepository.save(user);
    }

    @Override
    public ClinicAdministrator getLoginAdmin() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        try {
            ClinicAdministrator clinicAdministrator = clinicAdministratorRepository.findByEmail(currentUser.getName());
            if (clinicAdministrator != null) {
                return clinicAdministrator;
            }
        } catch (UsernameNotFoundException ex) {

        }
        return null;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public ClinicAdministratorDTO editPersonalInformation(EditClinicAdminDTO editClinicAdminDTO) {
        ClinicAdministrator clinicAdministrator = getLoginAdmin();

        if (clinicAdministrator.getId() != editClinicAdminDTO.getId()) {
            return null;
        }
        if (clinicAdministratorRepository.findByPhoneNumberAndIdNot(editClinicAdminDTO.getPhoneNumber(), editClinicAdminDTO.getId()) != null) {
            return null;
        }
        clinicAdministrator.setFirstName(editClinicAdminDTO.getFirstName());
        clinicAdministrator.setLastName(editClinicAdminDTO.getLastName());

        clinicAdministrator.setPhoneNumber(editClinicAdminDTO.getPhoneNumber());

        return new ClinicAdministratorDTO(clinicAdministratorRepository.save(clinicAdministrator));
    }

    @Override
    public EditClinicAdminDTO findClinicAdminById(Long id) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorRepository.getByIdAndStatusNot(id, UserStatus.NEVER_LOGGED_IN);
        return new EditClinicAdminDTO(clinicAdministrator);
    }

    @Override
    public List<ClinicAdministratorDTO> getAllClinicAdministratorsInClinic(Long id) {
        if (id == 0) {
            return convertToDTO(clinicAdministratorRepository.findAll());
        }

        List<ClinicAdministrator> adminsInClinic = clinicAdministratorRepository.findByClinicId(id);
        if (adminsInClinic == null) {
            return null;
        }

        return convertToDTO(adminsInClinic);
    }

    @Override
    @Transactional(readOnly = false)
    public ClinicAdministratorDTO create(ClinicAdministratorDTO clinicAdministratorDTO) {
        UserDetails userDetails = userService.findUserByEmail(clinicAdministratorDTO.getEmail());
        if (userDetails != null) {
            return null;
        }

        if (clinicAdministratorRepository.findByPhoneNumber(clinicAdministratorDTO.getPhoneNumber()) != null) {
            return null;
        }

        Clinic clinic = clinicService.findOneById(clinicAdministratorDTO.getClinic().getId());
        if (clinic == null) {
            return null;
        }

        RandomPasswordGenerator randomPasswordGenerator = new RandomPasswordGenerator();
        String generatedPassword = randomPasswordGenerator.generatePassword();
        String hashedPassword = passwordEncoder.encode(generatedPassword);

        Set<Authority> authorities = authenticationService.findByName("ROLE_CLINIC_ADMIN");

        ClinicAdministrator newClinicAdministrator = new ClinicAdministrator(clinicAdministratorDTO.getEmail(), hashedPassword,
                clinicAdministratorDTO.getFirstName(), clinicAdministratorDTO.getLastName(), clinicAdministratorDTO.getPhoneNumber(),
                clinic, authorities);

        ClinicAdministrator clinicAdministrator = clinicAdministratorRepository.save(newClinicAdministrator);

        composeAndSendEmail(clinicAdministrator.getEmail(), clinic.getName(), generatedPassword);

        return new ClinicAdministratorDTO(clinicAdministrator);
    }

    @Override
    public ClinicAdministrator findRandomAdminInClinic(Long clinicId) {
        List<ClinicAdministrator> clinicAdministrators = clinicAdministratorRepository.findByClinicIdAndStatusNot(clinicId, UserStatus.NEVER_LOGGED_IN);
        if (clinicAdministrators.isEmpty()) {
            return null;
        }
        return clinicAdministrators.get(new Random().nextInt(clinicAdministrators.size()));
    }

    @Override
    public ClinicAdministrator findByEmail(String email) {
        try {
            return clinicAdministratorRepository.findByEmail(email);
        } catch (UsernameNotFoundException ex) {
            return null;
        }
    }

    @Override
    public ClinicAdministrator findByPhoneNumber(String phoneNumber) {
        return clinicAdministratorRepository.findByPhoneNumber(phoneNumber);
    }

    private void composeAndSendEmail(String recipientEmail, String clinicName, String generatedPassword) {
        String subject = "New position: Clinic Administrator";
        StringBuilder sb = new StringBuilder();
        sb.append("You have been registered as a clinic administrator of a ");
        sb.append(clinicName);
        sb.append(" Clinic. From now on, you are in charge of managing the business of the clinic.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("You can log into the Clinical Centre System web site using your email address and the following password:");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("     ");
        sb.append(generatedPassword);
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Because of the security protocol, you will have to change this given password the first time you log in.");
        String text = sb.toString();

        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    private List<ClinicAdministratorDTO> convertToDTO(List<ClinicAdministrator> clinicAdmins) {
        List<ClinicAdministratorDTO> clinicAdminsDTO = new ArrayList<>();
        for (ClinicAdministrator clinicAdmin : clinicAdmins) {
            clinicAdminsDTO.add(new ClinicAdministratorDTO(clinicAdmin));
        }

        return clinicAdminsDTO;
    }
}
