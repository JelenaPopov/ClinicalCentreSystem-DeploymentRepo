package ftn.tim16.ClinicalCentreSystem.serviceimpl;

import ftn.tim16.ClinicalCentreSystem.common.RandomPasswordGenerator;
import ftn.tim16.ClinicalCentreSystem.dto.request.CreateDoctorDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.DoctorDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.EditDoctorDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.DoctorStatus;
import ftn.tim16.ClinicalCentreSystem.model.*;
import ftn.tim16.ClinicalCentreSystem.repository.DoctorRepository;
import ftn.tim16.ClinicalCentreSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockTimeoutException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private ExaminationService examinationService;

    @Autowired
    private ExaminationTypeService examinationTypeService;

    @Autowired
    private TimeOffDoctorService timeOffDoctorService;

    @Override
    @Transactional(readOnly = false)
    public Doctor changePassword(String newPassword, Doctor user) {
        if (user.getStatus().equals(DoctorStatus.DELETED)) {
            return null;
        }
        user.setPassword(newPassword);
        if (user.getStatus().equals(DoctorStatus.NEVER_LOGGED_IN)) {
            user.setStatus(DoctorStatus.ACTIVE);
        }
        return doctorRepository.save(user);
    }

    @Override
    public boolean isAvailable(Doctor doctor, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (doctor == null) {
            return false;
        }
        if (!doctor.isAvailable(startDateTime.toLocalTime(), endDateTime.toLocalTime())) {
            return false;
        }
        if (timeOffDoctorService.isDoctorOnVacation(doctor.getId(), startDateTime, endDateTime)) {
            return false;
        }
        List<Examination> examinations = examinationService.getDoctorExaminationsOnDay(doctor.getId(), startDateTime);

        if (!examinations.isEmpty()) {
            for (Examination examination : examinations) {
                if (!examination.getInterval().isAvailable(startDateTime, endDateTime)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean canGetTimeOff(Doctor loggedInDoctor, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Doctor doctor;
        try {
            doctor = findById(loggedInDoctor.getId());
        } catch (Exception e) {
            return false;
        }

        if (timeOffDoctorService.isDoctorOnVacation(doctor.getId(), startDateTime, endDateTime)) {
            return false;
        }

        List<Examination> examinations = examinationService.getDoctorExaminationsBetween(doctor.getId(), startDateTime, endDateTime);

        return (examinations == null || examinations.isEmpty());
    }

    @Override
    public Doctor getAvailableDoctor(ExaminationType specialized, LocalDateTime startDateTime, LocalDateTime endDateTime, Long clinicId) {
        List<Doctor> doctors = doctorRepository.findByClinicIdAndSpecializedIdAndStatusNot(clinicId, specialized.getId(), DoctorStatus.DELETED);
        for (Doctor doctor : doctors) {
            if (isAvailable(doctor, startDateTime, endDateTime)) {
                return doctor;
            }
        }
        return null;
    }

    @Override
    public Set<Doctor> getAvailableDoctors(ExaminationType specialized, LocalDateTime startDateTime, LocalDateTime endDateTime, Long clinicId) {
        Set<Doctor> availableDoctors = new HashSet<>();
        List<Doctor> doctors = doctorRepository.findByClinicIdAndSpecializedIdAndStatusNot(clinicId, specialized.getId(), DoctorStatus.DELETED);
        for (Doctor doctor : doctors) {
            if (isAvailable(doctor, startDateTime, endDateTime)) {
                availableDoctors.add(doctor);
            }
            if (availableDoctors.size() == 2) {
                break;
            }
        }

        return availableDoctors;
    }

    @Override
    @Transactional(readOnly = false)
    public void removeExamination(Examination examination, String email) {
        Doctor doc = doctorRepository.findByEmail(email);
        doc.getExaminations().remove(examination);
        doctorRepository.save(doc);
    }

    @Override
    public Doctor getLoginDoctor() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        try {
            Doctor doctor = doctorRepository.findByEmail(currentUser.getName());
            if (doctor != null) {
                return doctor;
            }
        } catch (UsernameNotFoundException ex) {
            return null;
        }
        return null;
    }

    private LocalDateTime getLocalDateTime(String date) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(date, formatter);
    }

    @Override
    public List<DoctorDTO> getAllAvailableDoctors(Long specializedId, Long clinicId, String startDateTime, String endDateTime) {
        List<Doctor> doctors = doctorRepository.findByStatusNotAndClinicIdAndSpecializedId(DoctorStatus.DELETED, clinicId, specializedId);
        List<DoctorDTO> availableDoctors = new ArrayList<>();
        for (Doctor doctor : doctors) {
            if (isAvailable(doctor, getLocalDateTime(startDateTime), getLocalDateTime(endDateTime))) {
                availableDoctors.add(new DoctorDTO(doctor));
            }
        }
        return availableDoctors;
    }

    @Override
    public Doctor getDoctor(Long id) {
        return doctorRepository.findByIdAndStatusNot(id, DoctorStatus.DELETED);
    }

    @Override
    public Doctor findById(Long id) throws LockTimeoutException {
        List<DoctorStatus> doctorStatuses = new ArrayList<>();
        doctorStatuses.add(DoctorStatus.NEVER_LOGGED_IN);
        doctorStatuses.add(DoctorStatus.ACTIVE);
        return doctorRepository.findByIdAndStatusIn(id, doctorStatuses);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public DoctorDTO deleteDoctor(Long clinicId, Long id) {

        Doctor doctor = getDoctor(id);
        if (doctor == null) {
            return null;
        }
        if (doctor.getClinic().getId() != clinicId || !isEditable(id)) {
            return null;
        }

        doctor.setStatus(DoctorStatus.DELETED);
        return new DoctorDTO(doctorRepository.save(doctor));
    }

    private boolean isEditable(Long doctorId) {
        List<Examination> upcomingExaminations = examinationService.getDoctorUpcomingExaminations(doctorId);

        if (upcomingExaminations != null && !upcomingExaminations.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public List<Doctor> findDoctorsByClinicIdAndExaminationTypeId(Long clinicId, Long specializedId) {
        return doctorRepository.findByStatusNotAndClinicIdAndSpecializedId(DoctorStatus.DELETED, clinicId, specializedId);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public DoctorDTO editPersonalInformation(EditDoctorDTO editDoctorDTO) {
        Doctor doctor = getLoginDoctor();

        if (doctor.getId() != editDoctorDTO.getId()) {
            return null;
        }

        LocalTime workHoursFrom = LocalTime.parse(editDoctorDTO.getWorkHoursFrom(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime workHoursTo = LocalTime.parse(editDoctorDTO.getWorkHoursTo(), DateTimeFormatter.ofPattern("HH:mm"));
        ExaminationType examinationType = examinationTypeService.findById(editDoctorDTO.getSpecialized().getId());
        if (workHoursFrom.isAfter(workHoursTo) || examinationType == null) {
            return null;
        }
        if (doctorRepository.findByPhoneNumberAndIdNot(editDoctorDTO.getPhoneNumber(), editDoctorDTO.getId()) != null) {
            return null;
        }
        if (!workHoursFrom.equals(doctor.getWorkHoursFrom()) || !workHoursTo.equals(doctor.getWorkHoursTo()) || doctor.getSpecialized().getId() != editDoctorDTO.getSpecialized().getId()) {
            if (!isEditable(editDoctorDTO.getId())) {
                return null;
            }
            doctor.setWorkHoursFrom(workHoursFrom);
            doctor.setWorkHoursTo(workHoursTo);
            doctor.setSpecialized(examinationType);
        }

        doctor.setFirstName(editDoctorDTO.getFirstName());
        doctor.setLastName(editDoctorDTO.getLastName());
        doctor.setPhoneNumber(editDoctorDTO.getPhoneNumber());
        return new DoctorDTO(doctorRepository.save(doctor));
    }

    @Override
    public EditDoctorDTO findDoctorById(Long id) {
        List<DoctorStatus> doctorStatuses = new ArrayList<>();
        doctorStatuses.add(DoctorStatus.NEVER_LOGGED_IN);
        doctorStatuses.add(DoctorStatus.DELETED);
        return new EditDoctorDTO(doctorRepository.findByIdAndStatusNotIn(id, doctorStatuses));
    }

    @Override
    public Doctor findByEmail(String email) {
        try {
            return doctorRepository.findByEmail(email);
        } catch (UsernameNotFoundException ex) {
            return null;
        }
    }

    @Override
    public Doctor findByPhoneNumber(String phoneNumber) {
        return doctorRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean haveToChangeDoctor(Examination assignedExamination, Doctor doctor, LocalDateTime startDateTime, LocalDateTime endDateTime) {

        if (doctor == null) {
            return false;
        }
        if (!doctor.isAvailable(startDateTime.toLocalTime(), endDateTime.toLocalTime())) {
            return false;
        }
        if (timeOffDoctorService.isDoctorOnVacation(doctor.getId(), startDateTime, endDateTime)) {
            return false;
        }
        List<Examination> examinations = examinationService.getDoctorExaminationsOnDay(doctor.getId(), startDateTime);

        if (!examinations.isEmpty()) {
            for (Examination examination : examinations) {
                if (examination.getId() != assignedExamination.getId() && !examination.getInterval().isAvailable(startDateTime, endDateTime)) {
                    return false;
                }
            }
        }
        return true;

    }

    @Override
    @Transactional(readOnly = false)
    public DoctorDTO create(CreateDoctorDTO doctor, ClinicAdministrator clinicAdministrator) throws DateTimeParseException {
        UserDetails userDetails = userService.findUserByEmail(doctor.getEmail());

        if (userDetails != null || doctorRepository.findByPhoneNumber(doctor.getPhoneNumber()) != null) {
            return null;
        }

        LocalTime workHoursFrom = LocalTime.parse(doctor.getWorkHoursFrom(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime workHoursTo = LocalTime.parse(doctor.getWorkHoursTo(), DateTimeFormatter.ofPattern("HH:mm"));
        ExaminationType examinationType = examinationTypeService.findById(doctor.getSpecialized().getId());
        if (workHoursFrom.isAfter(workHoursTo) || examinationType == null) {
            return null;
        }

        RandomPasswordGenerator randomPasswordGenerator = new RandomPasswordGenerator();
        String generatedPassword = randomPasswordGenerator.generatePassword();
        System.out.println(generatedPassword);
        String hashedPassword = passwordEncoder.encode(generatedPassword);

        Set<Authority> authorities = authenticationService.findByName("ROLE_DOCTOR");

        Doctor newDoctor = new Doctor(doctor.getEmail(), hashedPassword, doctor.getFirstName(),
                doctor.getLastName(), doctor.getPhoneNumber(), workHoursFrom, workHoursTo, clinicAdministrator.getClinic(),
                examinationType, DoctorStatus.NEVER_LOGGED_IN, authorities);

        Doctor savedDoctor = doctorRepository.save(newDoctor);

        composeAndSendEmail(savedDoctor.getEmail(), clinicAdministrator.getClinic().getName(), generatedPassword);

        return new DoctorDTO(savedDoctor);
    }

    @Override
    public List<DoctorDTO> findAllDoctorsInClinic(Clinic clinic) {
        return convertToDTO(doctorRepository.findByClinicIdAndStatusNot(clinic.getId(), DoctorStatus.DELETED));
    }

    @Override
    public List<DoctorDTO> searchDoctorsInClinic(Clinic clinic, String firstName, String lastName, String specializedFor) {
        return convertToDTO(doctorRepository.findByClinicIdAndStatusNotAndFirstNameContainsIgnoringCaseAndLastNameContainsIgnoringCaseAndSpecializedLabelContainsIgnoringCase(
                clinic.getId(), DoctorStatus.DELETED, firstName, lastName, specializedFor));
    }

    private void composeAndSendEmail(String recipientEmail, String clinicName, String generatedPassword) {
        String subject = "New position: Doctor";
        StringBuilder sb = new StringBuilder();
        sb.append("You have been registered as a doctor of a ");
        sb.append(clinicName);
        sb.append(" Clinic. From now on, you are in charge of examining patients and performing operations to them.");
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

    private List<DoctorDTO> convertToDTO(List<Doctor> doctors) {
        List<DoctorDTO> doctorDTOS = new ArrayList<>();
        for (Doctor doctor : doctors) {
            doctorDTOS.add(new DoctorDTO(doctor));
        }
        return doctorDTOS;
    }

}



