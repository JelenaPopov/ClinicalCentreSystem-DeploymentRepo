package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateTimeOffRequestDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.RequestForTimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.TimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffStatus;
import ftn.tim16.ClinicalCentreSystem.model.Nurse;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.util.List;

public interface TimeOffNurseService {

    TimeOffDTO create(Nurse nurse, CreateTimeOffRequestDTO timeOffRequestDTO);

    boolean isNurseOnVacation(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<TimeOffDTO> findByNurseIdAndStatus(Long id, TimeOffStatus status);

    List<RequestForTimeOffDTO> getRequestsForHolidayOrTimeOff(Long clinicId);

    RequestForTimeOffDTO approveRequestForHolidayOrTimeOff(Long id) throws OptimisticLockException;

    RequestForTimeOffDTO rejectRequestForHolidayOrTimeOff(Long id, String reason) throws OptimisticLockException;
}
