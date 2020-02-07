package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateTimeOffRequestDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.RequestForTimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.TimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffStatus;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;
import ftn.tim16.ClinicalCentreSystem.model.Nurse;
import ftn.tim16.ClinicalCentreSystem.service.ClinicAdministratorService;
import ftn.tim16.ClinicalCentreSystem.service.NurseService;
import ftn.tim16.ClinicalCentreSystem.service.TimeOffNurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/time-off-nurse")
public class TimeOffNurseController {

    @Autowired
    private NurseService nurseService;

    @Autowired
    private TimeOffNurseService timeOffNurseService;

    @Autowired
    private ClinicAdministratorService clinicAdministratorService;

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<List<TimeOffDTO>> getAllTimeOffForNurse() {
        Nurse nurse = nurseService.getLoginNurse();
        if (nurse == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            List<TimeOffDTO> timeOffNurses = timeOffNurseService.findByNurseIdAndStatus(nurse.getId(), TimeOffStatus.APPROVED);
            return new ResponseEntity<>(timeOffNurses, HttpStatus.OK);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<TimeOffDTO> createRequestForTimeOff(@Valid @RequestBody CreateTimeOffRequestDTO timeOffRequestDTO) {
        Nurse nurse = nurseService.getLoginNurse();
        if (nurse == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            TimeOffDTO createdTimeOff = timeOffNurseService.create(nurse, timeOffRequestDTO);
            if (createdTimeOff == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(createdTimeOff, HttpStatus.CREATED);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/requests-for-holiday-or-time-off")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<List<RequestForTimeOffDTO>> getRequestsForHolidayOrTimeOff() {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<RequestForTimeOffDTO> requests = timeOffNurseService.getRequestsForHolidayOrTimeOff(clinicAdministrator.getClinic().getId());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PutMapping(value = "/approve-request-for-holiday-or-time-off/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<RequestForTimeOffDTO> approveRequestForHolidayOrTimeOff(@PathVariable Long id) {
        try {
            RequestForTimeOffDTO request = timeOffNurseService.approveRequestForHolidayOrTimeOff(id);
            if (request == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(request, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping(value = "/reject-request-for-holiday-or-time-off/{id}")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<RequestForTimeOffDTO> rejectRequestForHolidayOrTimeOff(@RequestBody String reason, @PathVariable Long id) {
        try {
            RequestForTimeOffDTO request = timeOffNurseService.rejectRequestForHolidayOrTimeOff(id, reason);
            if (request == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(request, HttpStatus.OK);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
