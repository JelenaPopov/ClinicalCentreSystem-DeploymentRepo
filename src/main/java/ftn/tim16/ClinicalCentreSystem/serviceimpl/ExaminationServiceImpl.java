package ftn.tim16.ClinicalCentreSystem.serviceimpl;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateExaminationOrOperationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.request.PredefinedExaminationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.ExaminationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.ExaminationPagingDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.DoctorStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationKind;
import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationStatus;
import ftn.tim16.ClinicalCentreSystem.model.*;
import ftn.tim16.ClinicalCentreSystem.repository.ExaminationRepository;
import ftn.tim16.ClinicalCentreSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ExaminationServiceImpl implements ExaminationService {

    @Autowired
    private ExaminationRepository examinationRepository;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private ExaminationTypeService examinationTypeService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ClinicAdministratorService clinicAdministratorService;

    @Autowired
    private PatientService patientService;

    @Override
    public List<Examination> getExaminationsOnDay(Long idRoom, LocalDateTime day) {
        LocalDate date = getDate(day.toString());
        LocalDateTime greater = LocalDateTime.of(date, LocalTime.of(0, 0));

        LocalDateTime less = LocalDateTime.of(date, LocalTime.of(23, 59, 59));
        return examinationRepository.findByRoomIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan
                (idRoom, ExaminationStatus.CANCELED, greater, less);
    }

    @Override
    public List<Examination> getExaminations(Long idRoom) {
        return examinationRepository.findByRoomIdAndStatusNotOrderByIntervalStartDateTime(idRoom, ExaminationStatus.CANCELED);
    }

    @Override
    public List<Examination> getDoctorExaminations(Long idDoctor) {
        return examinationRepository.findByDoctorsIdAndStatusNot(idDoctor, ExaminationStatus.CANCELED);
    }

    @Override
    public List<Examination> getDoctorExaminationsOnDay(Long idDoctor, LocalDateTime day) {
        LocalDate date = getDate(day.toString());
        LocalDateTime greater = LocalDateTime.of(date, LocalTime.of(0, 0));

        LocalDateTime less = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        return examinationRepository.findByDoctorsIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan
                (idDoctor, ExaminationStatus.CANCELED, greater, less);
    }


    @Override
    public List<Examination> getNurseExaminations(Long idNurse) {
        return examinationRepository.findByNurseIdAndStatusNot(idNurse, ExaminationStatus.CANCELED);
    }

    @Override
    public List<Examination> getNurseExaminationsOnDay(Long idNurse, LocalDateTime day) {
        LocalDate date = getDate(day.toString());
        LocalDateTime greater = LocalDateTime.of(date, LocalTime.of(0, 0));

        LocalDateTime less = LocalDateTime.of(date, LocalTime.of(23, 59, 59));
        return examinationRepository.findByNurseIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan
                (idNurse, ExaminationStatus.CANCELED, greater, less);
    }

    @Override
    public Examination getExamination(Long id) {
        try {
            return examinationRepository.getByIdAndStatusNot(id, ExaminationStatus.CANCELED);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public ExaminationPagingDTO getAwaitingExaminations(String kind, ClinicAdministrator clinicAdministrator, Pageable page) {
        ExaminationKind examinationKind = getKind(kind);
        if (kind == null) {
            return null;
        }
        List<Examination> examinations = examinationRepository.findByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter
                (clinicAdministrator.getId(), ExaminationStatus.AWAITING, examinationKind, LocalDateTime.now());

        ExaminationPagingDTO examinationPagingDTO = new ExaminationPagingDTO(examinationRepository.findByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter
                (clinicAdministrator.getId(), ExaminationStatus.AWAITING, examinationKind, LocalDateTime.now(), page).getContent(), examinations.size());
        return examinationPagingDTO;
    }

    @Override
    public List<Examination> getAwaitingExaminations() {
        return examinationRepository.findByStatus(ExaminationStatus.AWAITING);
    }

    @Override
    public ExaminationPagingDTO getPredefinedExaminations(ClinicAdministrator clinicAdministrator, Pageable page) {
        Collection<ExaminationStatus> statuses = new ArrayList<>();
        statuses.add(ExaminationStatus.PREDEF_AVAILABLE);
        statuses.add(ExaminationStatus.PREDEF_BOOKED);

        List<Examination> examinations = examinationRepository.findByClinicAdministratorIdAndStatusIn
                (clinicAdministrator.getId(), statuses);
        Page<Examination> pageExaminations = examinationRepository.findByClinicAdministratorIdAndStatusIn
                (clinicAdministrator.getId(), statuses, page);
        ExaminationPagingDTO examinationPagingDTO = new ExaminationPagingDTO(pageExaminations.getContent(), examinations.size());
        return examinationPagingDTO;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Examination assignRoom(Examination selectedExamination, Room room, Nurse chosenNurse) {
        if (chosenNurse == null) {
            return null;
        }
        selectedExamination.setNurse(chosenNurse);
        selectedExamination.setRoom(room);
        selectedExamination.setStatus(ExaminationStatus.APPROVED);

        return examinationRepository.save(selectedExamination);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Examination assignRoomForOperation(Examination selectedExamination, Room room, Set<Doctor> doctors) {
        selectedExamination.setRoom(room);
        selectedExamination.setStatus(ExaminationStatus.APPROVED);
        selectedExamination.setDoctors(doctors);

        return examinationRepository.save(selectedExamination);
    }

    @Override
    public ExaminationPagingDTO getDoctorExaminations(Doctor doctor, Pageable page) {
        //Doctor can cancel examination that is more than 24 hours from starting.
        List<Examination> examinations = examinationRepository.findByDoctorsIdAndStatusNotAndIntervalStartDateTimeAfter
                (doctor.getId(), ExaminationStatus.CANCELED, LocalDateTime.now());

        ExaminationPagingDTO examinationPagingDTO = new ExaminationPagingDTO(examinationRepository.findByDoctorsIdAndStatusNotAndIntervalStartDateTimeAfter
                (doctor.getId(), ExaminationStatus.CANCELED, LocalDateTime.now(), page).getContent(), examinations.size());
        return examinationPagingDTO;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public ExaminationDTO cancelExamination(Doctor doctor, Long examinationId) {
        Examination examination = getExamination(examinationId);
        if (examination == null) {
            return null;
        }

        boolean foundDoctor = false;
        for (Doctor doc : examination.getDoctors()) {
            if (doc.getId() == doctor.getId()) {
                foundDoctor = true;
                break;
            }
        }

        if (!foundDoctor) {
            return null;
        }

        //Doctor can cancel scheduled examination/operation only 24 hours before it is going to be held.
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime examinationCanCancel = examination.getInterval().getStartDateTime().minusHours(24);
        if (currentTime.isAfter(examinationCanCancel)) {
            return null;
        }
        examination.setStatus(ExaminationStatus.CANCELED);
        examination.setDoctors(new HashSet<>());
        Nurse nurse = examination.getNurse();
        examination.setNurse(null);

        sendMail(examination, doctor, nurse, examination.getPatient());
        return new ExaminationDTO(examinationRepository.save(examination));
    }


    @Override
    @Transactional(readOnly = false)
    public ExaminationDTO createPredefinedExamination(PredefinedExaminationDTO predefinedExaminationDTO, ClinicAdministrator clinicAdministrator) {
        LocalDate localDate = getDate(predefinedExaminationDTO.getStartDateTime());
        LocalDateTime startDateTime = getLocalDateTime(localDate, predefinedExaminationDTO.getStartDateTime());
        LocalDateTime endDateTime = getLocalDateTime(localDate, predefinedExaminationDTO.getEndDateTime());

        if (startDateTime.isBefore(LocalDateTime.now()) || startDateTime.isAfter(endDateTime)) {
            return null;
        }

        ExaminationType examinationType = examinationTypeService.findById(predefinedExaminationDTO.getExaminationTypeDTO().getId());
        Doctor doctor;
        Room room;
        try {
            room = roomService.findById(predefinedExaminationDTO.getRoom());
            doctor = doctorService.findById(predefinedExaminationDTO.getDoctorDTO().getId());
        } catch (Exception p) {
            return null;
        }
        if (examinationType == null || doctor == null || room == null || examinationType.getId() != doctor.getSpecialized().getId()) {
            return null;
        }

        if (!doctorService.isAvailable(doctor, startDateTime, endDateTime) || !roomService.isAvailable(room, startDateTime, endDateTime)) {
            return null;
        }

        Nurse nurse = nurseService.getRandomNurse(clinicAdministrator.getClinic().getId(), startDateTime, endDateTime);

        if (nurse == null) {
            return null;
        }

        DateTimeInterval dateTimeInterval = new DateTimeInterval(startDateTime, endDateTime);
        Examination examination = new Examination(ExaminationKind.EXAMINATION, dateTimeInterval, ExaminationStatus.PREDEF_AVAILABLE, examinationType,
                room, predefinedExaminationDTO.getDiscount(), nurse, clinicAdministrator.getClinic(), clinicAdministrator);
        examination.getDoctors().add(doctor);

        return new ExaminationDTO(examinationRepository.save(examination));
    }

    @Override
    public List<Examination> getDoctorUpcomingExaminations(Long doctorId) {
        return examinationRepository.findByDoctorsIdAndStatusNotAndIntervalEndDateTimeAfter(doctorId, ExaminationStatus.CANCELED, LocalDateTime.now());
    }

    @Override
    public List<Examination> getNurseUpcomingExaminations(Long nurseId) {
        return examinationRepository.findByNurseIdAndStatusNotAndIntervalEndDateTimeAfter(nurseId, ExaminationStatus.CANCELED, LocalDateTime.now());
    }

    @Override
    public List<Examination> getUpcomingExaminationsInRoom(Long roomId) {
        return examinationRepository.findByRoomIdAndStatusNotAndIntervalEndDateTimeAfter(roomId, ExaminationStatus.CANCELED, LocalDateTime.now());
    }

    @Override
    public List<Examination> getUpcomingExaminationsOfExaminationType(Long examinationTypeId) {
        return examinationRepository.findByExaminationTypeIdAndStatusNotAndIntervalEndDateTimeAfter(examinationTypeId, ExaminationStatus.CANCELED, LocalDateTime.now());
    }

    @Override
    public Examination getOngoingExamination(Long patientId, Long doctorId, LocalDateTime examinationStartTime) {
        List<ExaminationStatus> statuses = new ArrayList<>();
        statuses.add(ExaminationStatus.PREDEF_BOOKED);
        statuses.add(ExaminationStatus.APPROVED);
        return examinationRepository.findByPatientIdAndDoctorsIdAndDoctorsStatusAndIntervalStartDateTimeLessThanEqualAndIntervalEndDateTimeGreaterThanAndStatusIn(
                patientId, doctorId, DoctorStatus.ACTIVE, examinationStartTime, examinationStartTime, statuses
        );
    }


    private LocalDateTime getLocalDateTime(LocalDate date, String time) throws DateTimeParseException {
        LocalTime localTime = LocalTime.parse(time.substring(11), DateTimeFormatter.ofPattern("HH:mm"));
        return LocalDateTime.of(date, localTime);
    }

    private LocalDate getDate(String date) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date.substring(0, 10), formatter);
    }


    private void sendMail(Examination examination, Doctor doctor, Nurse nurse, Patient patient) {
        if (patient == null) {
            return;
        }
        String subject = "Notice: Examination has been canceled ";
        StringBuilder sb = new StringBuilder();
        sb.append(doctor.getFirstName());
        sb.append(" ");
        sb.append(doctor.getLastName());
        sb.append(" has canceled the examination scheduled for ");
        sb.append(examination.getInterval().getStartDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm")));
        sb.append(".");

        String text = sb.toString();
        if (nurse != null) {
            emailNotificationService.sendEmail(nurse.getEmail(), subject, text);
        }

        emailNotificationService.sendEmail(patient.getEmail(), subject, text);
        if (examination.getKind().equals(ExaminationKind.OPERATION)) {
            for (Doctor doc : examination.getDoctors()) {
                if (doc.getId() != doctor.getId()) {
                    emailNotificationService.sendEmail(doc.getEmail(), subject, text);
                }
            }
        }
    }

    private ExaminationKind getKind(String kind) {
        try {
            return ExaminationKind.valueOf(kind.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Examination> getClinicExaminations(Long clinicId, LocalDateTime startDate, LocalDateTime endDateTime) {
        Collection<ExaminationStatus> statuses = new ArrayList<>();
        statuses.add(ExaminationStatus.APPROVED);
        statuses.add(ExaminationStatus.PREDEF_BOOKED);
        return examinationRepository.findByClinicIdAndStatusInAndIntervalStartDateTimeGreaterThanEqualAndIntervalEndDateTimeLessThan(clinicId, statuses, startDate, endDateTime);
    }

    @Override
    public List<Examination> getAllHeldExaminations(Long clinicId) {
        Collection<ExaminationStatus> statuses = new ArrayList<>();
        statuses.add(ExaminationStatus.APPROVED);
        statuses.add(ExaminationStatus.PREDEF_BOOKED);
        return examinationRepository.findByClinicIdAndStatusInAndIntervalEndDateTimeLessThan(clinicId, statuses, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = false)
    public ExaminationDTO createExaminationOrOperation(CreateExaminationOrOperationDTO createExaminationOrOperationDTO, Doctor loggedDoctor) {

        ExaminationKind kind = getKind(createExaminationOrOperationDTO.getKind());
        if (kind == null) {
            return null;
        }
        Patient patient = patientService.getPatient(createExaminationOrOperationDTO.getPatient().getId());
        if (patient == null) {
            return null;
        }
        LocalDate localDate = getDate(createExaminationOrOperationDTO.getStartDateTime());
        LocalDateTime startDateTime = getLocalDateTime(localDate, createExaminationOrOperationDTO.getStartDateTime());
        LocalDateTime endDateTime = getLocalDateTime(localDate, createExaminationOrOperationDTO.getEndDateTime());

        if (startDateTime.isBefore(LocalDateTime.now()) || startDateTime.isAfter(endDateTime)) {
            return null;
        }

        ExaminationType examinationType = examinationTypeService.findById(createExaminationOrOperationDTO.getExaminationType().getId());
        if (createExaminationOrOperationDTO.getKind().equals("EXAMINATION")) {
            Doctor doctor;
            try {
                doctor = doctorService.findById(createExaminationOrOperationDTO.getDoctor().getId());
            } catch (Exception e) {
                return null;
            }
            if (examinationType == null || doctor == null || examinationType.getId() != doctor.getSpecialized().getId() || !doctorService.isAvailable(doctor, startDateTime, endDateTime)) {
                return null;
            }

            DateTimeInterval dateTimeInterval = new DateTimeInterval(startDateTime, endDateTime);
            ClinicAdministrator clinicAdministrator = clinicAdministratorService.findRandomAdminInClinic(loggedDoctor.getClinic().getId());
            if (clinicAdministrator == null) {
                return null;
            }
            Examination examination = new Examination(kind, dateTimeInterval, ExaminationStatus.AWAITING, examinationType,
                    null, 0, null, loggedDoctor.getClinic(), clinicAdministrator, patient);
            examination.getDoctors().add(doctor);
            sendMailToClinicAdministrator(examination, loggedDoctor, clinicAdministrator);
            return new ExaminationDTO(examinationRepository.save(examination));
        }
        if (examinationType == null) {
            return null;
        }
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.findRandomAdminInClinic(loggedDoctor.getClinic().getId());
        if (clinicAdministrator == null) {
            return null;
        }
        DateTimeInterval dateTimeInterval = new DateTimeInterval(startDateTime, endDateTime);
        Examination examination = new Examination(kind, dateTimeInterval, ExaminationStatus.AWAITING, examinationType,
                null, 0, null, loggedDoctor.getClinic(), clinicAdministrator, patient);

        sendMailToClinicAdministrator(examination, loggedDoctor, clinicAdministrator);
        return new ExaminationDTO(examinationRepository.save(examination));
    }

    @Override
    public List<Examination> getExaminationsAfter(Long idRoom, LocalDateTime endDateTime) {
        return examinationRepository.findByRoomIdAndStatusNotAndIntervalEndDateTimeGreaterThanEqualOrderByIntervalStartDateTime(
                idRoom, ExaminationStatus.CANCELED, endDateTime
        );
    }

    @Override
    public List<Examination> getDoctorExaminationsBetween(Long doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return examinationRepository.findByDoctorsIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan(doctorId, ExaminationStatus.CANCELED, startDateTime, endDateTime);
    }

    @Override
    public List<Examination> getNurseExaminationsBetween(Long nurseId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return examinationRepository.findByNurseIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan(nurseId, ExaminationStatus.CANCELED, startDateTime, endDateTime);
    }

    @Override
    public boolean hasDoctorHeldExaminationForPatient(Doctor doctor, Patient patient) {
        Collection<ExaminationStatus> statuses = new ArrayList<>();
        statuses.add(ExaminationStatus.APPROVED);
        statuses.add(ExaminationStatus.PREDEF_BOOKED);
        List<Examination> examinations = examinationRepository.findByClinicIdAndStatusInAndDoctorsIdAndPatientIdAndIntervalStartDateTimeLessThanEqual(doctor.getClinic().getId(),
                statuses, doctor.getId(), patient.getId(), LocalDateTime.now());
        return !examinations.isEmpty();
    }

    @Override
    public boolean hasNurseHeldExaminationForPatient(Nurse nurse, Patient patient) {
        Collection<ExaminationStatus> statuses = new ArrayList<>();
        statuses.add(ExaminationStatus.APPROVED);
        statuses.add(ExaminationStatus.PREDEF_BOOKED);
        List<Examination> examinations = examinationRepository.findByClinicIdAndStatusInAndNurseIdAndPatientIdAndIntervalStartDateTimeLessThanEqual(nurse.getClinic().getId(),
                statuses, nurse.getId(), patient.getId(), LocalDateTime.now());
        return !examinations.isEmpty();
    }

    private void sendMailToClinicAdministrator(Examination examination, Doctor doctor, ClinicAdministrator clinicAdministrator) {
        if (clinicAdministrator == null || doctor == null || examination == null) {
            return;
        }
        String subject = "Notice: Examination request has been created ";
        StringBuilder sb = new StringBuilder();
        sb.append(doctor.getFirstName());
        sb.append(" ");
        sb.append(doctor.getLastName());
        sb.append(" has created the ");
        sb.append(examination.getKind().toString().toLowerCase());
        sb.append(" scheduled for ");
        sb.append(examination.getInterval().getStartDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm")));
        sb.append(".");

        String text = sb.toString();
        emailNotificationService.sendEmail(clinicAdministrator.getEmail(), subject, text);
    }
}
