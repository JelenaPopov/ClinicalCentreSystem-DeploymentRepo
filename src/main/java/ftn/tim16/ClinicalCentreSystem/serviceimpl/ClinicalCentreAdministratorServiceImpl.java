package ftn.tim16.ClinicalCentreSystem.serviceimpl;

import ftn.tim16.ClinicalCentreSystem.common.RandomPasswordGenerator;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ClinicalCentreAdminDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.UserStatus;
import ftn.tim16.ClinicalCentreSystem.model.Authority;
import ftn.tim16.ClinicalCentreSystem.model.ClinicalCentreAdministrator;
import ftn.tim16.ClinicalCentreSystem.repository.ClinicalCentreAdministratorRepository;
import ftn.tim16.ClinicalCentreSystem.service.AuthenticationService;
import ftn.tim16.ClinicalCentreSystem.service.ClinicalCentreAdministratorService;
import ftn.tim16.ClinicalCentreSystem.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ClinicalCentreAdministratorServiceImpl implements ClinicalCentreAdministratorService {

    @Autowired
    private ClinicalCentreAdministratorRepository clinicalCentreAdminRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Transactional(readOnly = false)
    @EventListener(ApplicationReadyEvent.class)
    public void insertAfterStartup() {
        Set<Authority> authorities = authenticationService.findByName("ROLE_CLINICAL_CENTRE_ADMIN");
        // Initial password: 1st.Admin
        ClinicalCentreAdministrator clinicalCentreAdministrator = new ClinicalCentreAdministrator("1st.Admin@maildrop.cc", "$2a$10$JA.M/IQm9r29csrRlkNSteO/k4q3MclGtWfW/MjVqKFMVKb9T.F0i", "Stefan", "Stefanovic", "061123456", authorities);
        clinicalCentreAdministrator.setStatus(UserStatus.ACTIVE);
        if (userService.findUserByEmail(clinicalCentreAdministrator.getEmail()) != null) {
            return;
        }

        clinicalCentreAdminRepository.save(clinicalCentreAdministrator);
    }

    @Override
    @Transactional(readOnly = false)
    public ClinicalCentreAdministrator changePassword(String newPassword, ClinicalCentreAdministrator user) {
        user.setPassword(newPassword);
        if (user.getStatus().equals(UserStatus.NEVER_LOGGED_IN)) {
            user.setStatus(UserStatus.ACTIVE);
        }
        return clinicalCentreAdminRepository.save(user);
    }

    @Override
    public ClinicalCentreAdministrator getLoginAdmin() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        try {
            ClinicalCentreAdministrator clinicalCentreAdministrator = clinicalCentreAdminRepository.findByEmail(currentUser.getName());
            if (clinicalCentreAdministrator != null) {
                return clinicalCentreAdministrator;
            }
        } catch (UsernameNotFoundException ex) {

        }
        return null;
    }

    @Override
    public List<ClinicalCentreAdminDTO> getAllClinicalCentreAdministrators(Long clinicalCentreAdminId) {
        return convertToDTO(clinicalCentreAdminRepository.findByIdNot(clinicalCentreAdminId));
    }

    @Override
    @Transactional(readOnly = false)
    public ClinicalCentreAdminDTO create(ClinicalCentreAdminDTO clinicalCentreAdminDTO) {
        UserDetails userDetails = userService.findUserByEmail(clinicalCentreAdminDTO.getEmail());
        if (userDetails != null) {
            return null;
        }

        if (clinicalCentreAdminRepository.findByPhoneNumber(clinicalCentreAdminDTO.getPhoneNumber()) != null) {
            return null;
        }

        RandomPasswordGenerator randomPasswordGenerator = new RandomPasswordGenerator();
        String generatedPassword = randomPasswordGenerator.generatePassword();
        String hashedPassword = passwordEncoder.encode(generatedPassword);

        Set<Authority> authorities = authenticationService.findByName("ROLE_CLINICAL_CENTRE_ADMIN");

        ClinicalCentreAdministrator newClinicalCentreAdmin = new ClinicalCentreAdministrator(clinicalCentreAdminDTO.getEmail(), hashedPassword,
                clinicalCentreAdminDTO.getFirstName(), clinicalCentreAdminDTO.getLastName(),
                clinicalCentreAdminDTO.getPhoneNumber(), authorities);

        ClinicalCentreAdministrator clinicalCentreAdministrator = clinicalCentreAdminRepository.save(newClinicalCentreAdmin);

        composeAndSendEmail(clinicalCentreAdministrator.getEmail(), generatedPassword);

        return new ClinicalCentreAdminDTO(clinicalCentreAdminRepository.save(clinicalCentreAdministrator));
    }

    @Override
    public ClinicalCentreAdministrator findByEmail(String email) {
        try {
            return clinicalCentreAdminRepository.findByEmail(email);
        } catch (UsernameNotFoundException ex) {
            return null;
        }
    }

    @Override
    public ClinicalCentreAdministrator findByPhoneNumber(String phoneNumber) {
        return clinicalCentreAdminRepository.findByPhoneNumber(phoneNumber);
    }

    private void composeAndSendEmail(String recipientEmail, String generatedPassword) {
        String subject = "New position: Clinical Centre Administrator";
        StringBuilder sb = new StringBuilder();
        sb.append("You have been registered as a clinical centre administrator of the Clinical Centre System. ");
        sb.append("From now on, you are in charge of reviewing patients' requests to register, adding new clinics and clinic administrators ");
        sb.append("and creating codebooks of diagnosis and medicines.");
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

    private List<ClinicalCentreAdminDTO> convertToDTO(List<ClinicalCentreAdministrator> administrators) {
        List<ClinicalCentreAdminDTO> administratorsDTO = new ArrayList<>();
        for (ClinicalCentreAdministrator admin : administrators) {
            administratorsDTO.add(new ClinicalCentreAdminDTO(admin));
        }

        return administratorsDTO;
    }
}
