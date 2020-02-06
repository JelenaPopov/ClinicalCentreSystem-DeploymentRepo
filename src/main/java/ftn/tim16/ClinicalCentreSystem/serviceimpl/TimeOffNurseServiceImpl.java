package ftn.tim16.ClinicalCentreSystem.serviceimpl;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateTimeOffRequestDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.RequestForTimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.TimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffType;
import ftn.tim16.ClinicalCentreSystem.model.DateTimeInterval;
import ftn.tim16.ClinicalCentreSystem.model.Nurse;
import ftn.tim16.ClinicalCentreSystem.model.TimeOffNurse;
import ftn.tim16.ClinicalCentreSystem.repository.TimeOffNurseRepository;
import ftn.tim16.ClinicalCentreSystem.service.EmailNotificationService;
import ftn.tim16.ClinicalCentreSystem.service.NurseService;
import ftn.tim16.ClinicalCentreSystem.service.TimeOffNurseService;
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
public class TimeOffNurseServiceImpl implements TimeOffNurseService {

    @Autowired
    private TimeOffNurseRepository timeOffNurseRepository;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Override
    @Transactional(readOnly = false)
    public TimeOffDTO create(Nurse nurse, CreateTimeOffRequestDTO timeOffRequestDTO) {
        LocalDateTime startDateTime = getLocalDateTime(timeOffRequestDTO.getStartDateTime());
        LocalDateTime endDateTime = getLocalDateTime(timeOffRequestDTO.getEndDateTime());

        if (startDateTime.isBefore(LocalDateTime.now()) || startDateTime.isAfter(endDateTime)) {
            return null;
        }
        if (!nurseService.canGetTimeOff(nurse, startDateTime, endDateTime)) {
            return null;
        }

        TimeOffType timeOffType = getTimeOffType(timeOffRequestDTO.getType());
        if (timeOffType == null) {
            return null;
        }
        DateTimeInterval dateTimeInterval = new DateTimeInterval(startDateTime, endDateTime);

        TimeOffNurse timeOffNurse = new TimeOffNurse(timeOffType, dateTimeInterval, TimeOffStatus.AWAITING, nurse);

        return new TimeOffDTO(timeOffNurseRepository.save(timeOffNurse));
    }

    @Override
    public boolean isNurseOnVacation(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<TimeOffNurse> timeOffNurses = timeOffNurseRepository.findByNurseIdAndStatusNot(id, TimeOffStatus.REJECTED);
        if (!timeOffNurses.isEmpty()) {
            for (TimeOffNurse timeOffNurse : timeOffNurses) {
                if (!timeOffNurse.getInterval().isAvailable(startDateTime, endDateTime)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<TimeOffDTO> findByNurseIdAndStatus(Long id, TimeOffStatus status) {
        return convertToDTO(timeOffNurseRepository.findByNurseIdAndStatus(id, status));
    }

    @Override
    public List<RequestForTimeOffDTO> getRequestsForHolidayOrTimeOff(Long clinicId) {
        return convertRequestToDTO(timeOffNurseRepository.findByNurseClinicIdAndStatus(clinicId, TimeOffStatus.AWAITING));
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public RequestForTimeOffDTO approveRequestForHolidayOrTimeOff(Long id) throws OptimisticLockException {
        TimeOffNurse timeOffNurse = timeOffNurseRepository.findByIdAndStatus(id, TimeOffStatus.AWAITING);

        if (timeOffNurse == null) {
            return null;
        }

        timeOffNurse.setStatus(TimeOffStatus.APPROVED);

        TimeOffNurse updatedTimeOff = timeOffNurseRepository.save(timeOffNurse);

        composeAndSendApprovalEmail(updatedTimeOff.getNurse().getEmail(), updatedTimeOff.getType().toString());

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
        TimeOffNurse timeOffNurse = timeOffNurseRepository.findByIdAndStatus(id, TimeOffStatus.AWAITING);

        if (timeOffNurse == null) {
            return null;
        }

        timeOffNurse.setStatus(TimeOffStatus.REJECTED);
        TimeOffNurse updatedTimeOff = timeOffNurseRepository.save(timeOffNurse);

        composeAndSendRejectionEmail(updatedTimeOff.getNurse().getEmail(), updatedTimeOff.getType().toString(), reason);
        return new RequestForTimeOffDTO(updatedTimeOff);
    }

    private void composeAndSendRejectionEmail(String recipientEmail, String type, String reason) {

        String subject = "Request for holiday/time off rejected";
        StringBuilder sb = new StringBuilder();
        sb.append(" Your request for ");
        sb.append(type.toLowerCase());
        sb.append(" is rejected by a clinic administrator.");
        sb.append(System.lineSeparator());
        sb.append("Explanation:");
        sb.append(System.lineSeparator());
        sb.append(reason);
        String text = sb.toString();

        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    private List<TimeOffDTO> convertToDTO(List<TimeOffNurse> timeOffNurses) {
        if (timeOffNurses == null || timeOffNurses.isEmpty()) {
            return new ArrayList<>();
        }
        List<TimeOffDTO> timeOffDTOS = new ArrayList<>();
        for (TimeOffNurse timeOffNurse : timeOffNurses) {
            timeOffDTOS.add(new TimeOffDTO(timeOffNurse));
        }
        return timeOffDTOS;
    }

    private List<RequestForTimeOffDTO> convertRequestToDTO(List<TimeOffNurse> timeOffNurses) {
        if (timeOffNurses == null || timeOffNurses.isEmpty()) {
            return new ArrayList<>();
        }
        List<RequestForTimeOffDTO> timeOffDTOS = new ArrayList<>();
        for (TimeOffNurse timeOffNurse : timeOffNurses) {
            timeOffDTOS.add(new RequestForTimeOffDTO(timeOffNurse));
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
