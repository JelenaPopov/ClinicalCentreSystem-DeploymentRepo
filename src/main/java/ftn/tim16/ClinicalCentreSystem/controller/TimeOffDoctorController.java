package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateTimeOffRequestDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.RequestForTimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.TimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffStatus;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;
import ftn.tim16.ClinicalCentreSystem.model.Doctor;
import ftn.tim16.ClinicalCentreSystem.service.ClinicAdministratorService;
import ftn.tim16.ClinicalCentreSystem.service.DoctorService;
import ftn.tim16.ClinicalCentreSystem.service.TimeOffDoctorService;
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
@RequestMapping(value = "/api/time-off-doctor")
public class TimeOffDoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private TimeOffDoctorService timeOffDoctorService;

    @Autowired
    private ClinicAdministratorService clinicAdministratorService;

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<TimeOffDTO>> getAllTimeOffForDoctor() {
        Doctor doctor = doctorService.getLoginDoctor();
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            List<TimeOffDTO> timeOffDoctors = timeOffDoctorService.findByDoctorIdAndStatus(doctor.getId(), TimeOffStatus.APPROVED);
            return new ResponseEntity<>(timeOffDoctors, HttpStatus.OK);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<TimeOffDTO> createRequestForTimeOff(@Valid @RequestBody CreateTimeOffRequestDTO timeOffRequestDTO) {
        Doctor doctor = doctorService.getLoginDoctor();
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            TimeOffDTO createdTimeOff = timeOffDoctorService.create(doctor, timeOffRequestDTO);
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
        List<RequestForTimeOffDTO> requests = timeOffDoctorService.getRequestsForHolidayOrTimeOff(clinicAdministrator.getClinic().getId());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PutMapping(value = "/approve-request-for-holiday-or-time-off/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<RequestForTimeOffDTO> approveRequestForHolidayOrTimeOff(@PathVariable Long id) {
        try {
            RequestForTimeOffDTO request = timeOffDoctorService.approveRequestForHolidayOrTimeOff(id);
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
            RequestForTimeOffDTO request = timeOffDoctorService.rejectRequestForHolidayOrTimeOff(id, reason);
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
