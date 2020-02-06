package ftn.tim16.ClinicalCentreSystem.serviceimpl;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateTimeOffRequestDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.RequestForTimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.TimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffType;
import ftn.tim16.ClinicalCentreSystem.model.DateTimeInterval;
import ftn.tim16.ClinicalCentreSystem.model.Doctor;
import ftn.tim16.ClinicalCentreSystem.model.TimeOffDoctor;
import ftn.tim16.ClinicalCentreSystem.repository.TimeOffDoctorRepository;
import ftn.tim16.ClinicalCentreSystem.service.DoctorService;
import ftn.tim16.ClinicalCentreSystem.service.EmailNotificationService;
import ftn.tim16.ClinicalCentreSystem.service.TimeOffDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TimeOffDoctorServiceImpl implements TimeOffDoctorService {

    @Autowired
    private TimeOffDoctorRepository timeOffDoctorRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Override
    @Transactional(readOnly = false)
    public TimeOffDTO create(Doctor doctor, CreateTimeOffRequestDTO timeOffRequestDTO) throws DateTimeParseException {
        LocalDateTime startDateTime = getLocalDateTime(timeOffRequestDTO.getStartDateTime());
        LocalDateTime endDateTime = getLocalDateTime(timeOffRequestDTO.getEndDateTime());

        if (startDateTime.isBefore(LocalDateTime.now()) || startDateTime.isAfter(endDateTime)) {
            return null;
        }
        if (!doctorService.canGetTimeOff(doctor, startDateTime, endDateTime)) {
            return null;
        }

        TimeOffType timeOffType = getTimeOffType(timeOffRequestDTO.getType());
        if (timeOffType == null) {
            return null;
        }
        DateTimeInterval dateTimeInterval = new DateTimeInterval(startDateTime, endDateTime);

        TimeOffDoctor timeOffDoctor = new TimeOffDoctor(timeOffType, dateTimeInterval, TimeOffStatus.AWAITING, doctor);

        return new TimeOffDTO(timeOffDoctorRepository.save(timeOffDoctor));
    }

    @Override
    public boolean isDoctorOnVacation(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<TimeOffDoctor> timeOffDoctors = timeOffDoctorRepository.findByDoctorIdAndStatusNot(id, TimeOffStatus.REJECTED);
        if (!timeOffDoctors.isEmpty()) {
            for (TimeOffDoctor timeOffDoctor : timeOffDoctors) {
                if (!timeOffDoctor.getInterval().isAvailable(startDateTime, endDateTime)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<TimeOffDTO> findByDoctorIdAndStatus(Long id, TimeOffStatus status) {
        return convertToDTO(timeOffDoctorRepository.findByDoctorIdAndStatus(id, status));
    }

    @Override
    public List<RequestForTimeOffDTO> getRequestsForHolidayOrTimeOff(Long clinicId) {
        return convertRequestToDTO(timeOffDoctorRepository.findByDoctorClinicIdAndStatus(clinicId, TimeOffStatus.AWAITING));
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public RequestForTimeOffDTO approveRequestForHolidayOrTimeOff(Long id) throws OptimisticLockException {
        TimeOffDoctor timeOffDoctor = timeOffDoctorRepository.findByIdAndStatus(id, TimeOffStatus.AWAITING);

        if (timeOffDoctor == null) {
            return null;
        }

        timeOffDoctor.setStatus(TimeOffStatus.APPROVED);

        TimeOffDoctor updatedTimeOff = timeOffDoctorRepository.save(timeOffDoctor);

        composeAndSendApprovalEmail(updatedTimeOff.getDoctor().getEmail(), updatedTimeOff.getType().toString());
        return new RequestForTimeOffDTO(updatedTimeOff);
    }

    private void composeAndSendApprovalEmail(String recipientEmail, String type) {
        String subject = "Request for holiday/time off approved";
        StringBuilder sb = new StringBuilder();
        sb.append("Great news! Your request for ");
        sb.append(type.toLowerCase());
        sb.append(" is approved by a clinic administrator.");
        String text = sb.toString();

        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public RequestForTimeOffDTO rejectRequestForHolidayOrTimeOff(Long id, String reason) throws OptimisticLockException {
        TimeOffDoctor timeOffDoctor = timeOffDoctorRepository.findByIdAndStatus(id, TimeOffStatus.AWAITING);
        if (timeOffDoctor == null) {
            return null;
        }

        timeOffDoctor.setStatus(TimeOffStatus.REJECTED);
        TimeOffDoctor updatedTimeOff = timeOffDoctorRepository.save(timeOffDoctor);

        composeAndSendRejectionEmail(updatedTimeOff.getDoctor().getEmail(), updatedTimeOff.getType().toString(), reason);

        return new RequestForTimeOffDTO(updatedTimeOff);
    }

    private void composeAndSendRejectionEmail(String recipientEmail, String type, String reason) {

        String subject = "Request for holiday/time off rejected ";
        StringBuilder sb = new StringBuilder();
        sb.append(" Your request for");
        sb.append(type.toLowerCase());
        sb.append(" is rejected by a clinic administrator.");
        sb.append(System.lineSeparator());
        sb.append("Explanation:");
        sb.append(System.lineSeparator());
        sb.append(reason);
        String text = sb.toString();

        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    private List<TimeOffDTO> convertToDTO(List<TimeOffDoctor> timeOffDoctors) {
        if (timeOffDoctors == null || timeOffDoctors.isEmpty()) {
            return new ArrayList<>();
        }
        List<TimeOffDTO> timeOffDTOS = new ArrayList<>();
        for (TimeOffDoctor timeOffDoctor : timeOffDoctors) {
            timeOffDTOS.add(new TimeOffDTO(timeOffDoctor));
        }
        return timeOffDTOS;
    }

    private List<RequestForTimeOffDTO> convertRequestToDTO(List<TimeOffDoctor> timeOffDoctors) {
        if (timeOffDoctors == null || timeOffDoctors.isEmpty()) {
            return new ArrayList<>();
        }
        List<RequestForTimeOffDTO> timeOffDTOS = new ArrayList<>();
        for (TimeOffDoctor timeOffDoctor : timeOffDoctors) {
            timeOffDTOS.add(new RequestForTimeOffDTO(timeOffDoctor));
        }
        return timeOffDTOS;
    }

    private LocalDateTime getLocalDateTime(String date) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(date, formatter);
    }

    private TimeOffType getTimeOffType(String type) {
        if (type.equalsIgnoreCase("HOLIDAY")) {
            return TimeOffType.HOLIDAY;
        } else if (type.equalsIgnoreCase("TIME OFF")) {
            return TimeOffType.TIME_OFF;
        }

        return null;
    }
}
