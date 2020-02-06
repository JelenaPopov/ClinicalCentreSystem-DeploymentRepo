package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateTimeOffRequestDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.RequestForTimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.TimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffStatus;
import ftn.tim16.ClinicalCentreSystem.model.Doctor;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.util.List;

public interface TimeOffDoctorService {

    TimeOffDTO create(Doctor doctor, CreateTimeOffRequestDTO timeOffRequestDTO);

    boolean isDoctorOnVacation(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<TimeOffDTO> findByDoctorIdAndStatus(Long id, TimeOffStatus status);

    List<RequestForTimeOffDTO> getRequestsForHolidayOrTimeOff(Long clinicId);

    RequestForTimeOffDTO approveRequestForHolidayOrTimeOff(Long id) throws OptimisticLockException;

    RequestForTimeOffDTO rejectRequestForHolidayOrTimeOff(Long id, String reason) throws OptimisticLockException;

}
