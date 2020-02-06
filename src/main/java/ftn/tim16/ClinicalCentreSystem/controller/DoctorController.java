package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateDoctorDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.DoctorDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.EditDoctorDTO;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;
import ftn.tim16.ClinicalCentreSystem.model.Doctor;
import ftn.tim16.ClinicalCentreSystem.service.ClinicAdministratorService;
import ftn.tim16.ClinicalCentreSystem.service.DoctorService;
import ftn.tim16.ClinicalCentreSystem.service.ExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ClinicAdministratorService clinicAdministratorService;

    @Autowired
    private ExaminationService examinationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<DoctorDTO> create(@Valid @RequestBody CreateDoctorDTO doctor) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            DoctorDTO createdDoctor = doctorService.create(doctor, clinicAdministrator);
            if (createdDoctor == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<List<DoctorDTO>> getAllDoctorsForAdmin() {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(doctorService.findAllDoctorsInClinic(clinicAdministrator.getClinic()), HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<List<DoctorDTO>> searchDoctorsInClinic(@RequestParam(value = "firstName") String firstName,
                                                                 @RequestParam(value = "lastName") String lastName,
                                                                 @RequestParam(value = "specializedFor") String specializedFor) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(doctorService.searchDoctorsInClinic(clinicAdministrator.getClinic(), firstName, lastName, specializedFor), HttpStatus.OK);
    }

    @GetMapping(value = "/available")
    @PreAuthorize("hasAnyRole('CLINIC_ADMIN','DOCTOR')")
    public ResponseEntity<List<DoctorDTO>> getAllAvailableDoctors(@RequestParam(value = "specialized", required = true) Long specializedId,
                                                                  @RequestParam(value = "startDateTime", required = true) String startDateTime,
                                                                  @RequestParam(value = "endDateTime", required = true) String endDateTime) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            Doctor doctor = doctorService.getLoginDoctor();
            if (doctor == null) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(doctorService.getAllAvailableDoctors(specializedId, doctor.getClinic().getId(), startDateTime, endDateTime), HttpStatus.OK);
        }
        return new ResponseEntity<>(doctorService.getAllAvailableDoctors(specializedId, clinicAdministrator.getClinic().getId(), startDateTime, endDateTime), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<DoctorDTO> deleteDoctor(@PathVariable("id") Long id) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        DoctorDTO doctor = doctorService.deleteDoctor(clinicAdministrator.getClinic().getId(), id);
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(doctor, HttpStatus.ACCEPTED);

    }

    @GetMapping(value = "/{id}")
    public EditDoctorDTO getDoctor(@PathVariable Long id) {

        return doctorService.findDoctorById(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorDTO> editPersonalInformation(@Valid @RequestBody EditDoctorDTO doctorDTO) {
        DoctorDTO doctor = doctorService.editPersonalInformation(doctorDTO);
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(doctor, HttpStatus.CREATED);
    }

    @GetMapping(value = "/is-available")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<Boolean> isAvailable(@RequestParam(value = "examinationId", required = true) String examinationId,
                                               @RequestParam(value = "doctorId", required = true) String id,
                                               @RequestParam(value = "startTime") String searchStartTime,
                                               @RequestParam(value = "endTime") String searchEndTime) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            Long doctorId = Long.parseLong(id);
            boolean isAvailable = doctorService.haveToChangeDoctor(examinationService.getExamination(Long.parseLong(examinationId)),
                    doctorService.getDoctor(doctorId), LocalDateTime.parse(searchStartTime, formatter),
                    LocalDateTime.parse(searchEndTime, formatter));
            return new ResponseEntity<>(isAvailable, HttpStatus.OK);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException num) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
