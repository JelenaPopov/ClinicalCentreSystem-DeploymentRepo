package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateExaminationOrOperationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.request.PredefinedExaminationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.ExaminationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.ExaminationForWorkCalendarDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.ExaminationPagingDTO;
import ftn.tim16.ClinicalCentreSystem.model.*;
import ftn.tim16.ClinicalCentreSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/examination")
public class ExaminationController {

    @Autowired
    private ExaminationService examinationService;

    @Autowired
    private ClinicAdministratorService clinicAdministratorService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private PatientService patientService;

    @GetMapping(value = "/starting/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ExaminationDTO> getPatientStartingExamination(@PathVariable("id") Long patientId) {
        Doctor doctor = doctorService.getLoginDoctor();
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Patient patient = patientService.getPatient(patientId);
        if (patient == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LocalDateTime examinationStartTime = LocalDateTime.now();
        Examination ongoingExamination = examinationService.getOngoingExamination(patient.getId(), doctor.getId(), examinationStartTime);
        if (ongoingExamination == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ExaminationDTO(ongoingExamination), HttpStatus.OK);
    }

    @GetMapping(value = "/get-awaiting")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<ExaminationPagingDTO> getAwaitingExaminations(@RequestParam(value = "kind") String kind, Pageable page) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            return new ResponseEntity<>(examinationService.getAwaitingExaminations(kind, clinicAdministrator, page), HttpStatus.OK);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/get-doctors-examinations")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ExaminationPagingDTO> getDoctorExaminations(Pageable page) {
        Doctor doctor = doctorService.getLoginDoctor();
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            return new ResponseEntity<>(examinationService.getDoctorExaminations(doctor, page), HttpStatus.OK);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cancel/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ExaminationDTO> cancelExamination(@PathVariable("id") Long examinationId) {
        Doctor doctor = doctorService.getLoginDoctor();
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ExaminationDTO examination = examinationService.cancelExamination(doctor, examinationId);
        if (examination == null) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(examination, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/doctor-examinations")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<ExaminationForWorkCalendarDTO>> getDoctorExaminationsForWorkCalendar() {
        Doctor doctor = doctorService.getLoginDoctor();
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            List<ExaminationForWorkCalendarDTO> examinationForWorkCalendarDTOS = convertToDTO(examinationService.getDoctorExaminations(doctor.getId()));
            return new ResponseEntity<>(examinationForWorkCalendarDTOS, HttpStatus.OK);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/nurse-examinations")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<List<ExaminationForWorkCalendarDTO>> getNurseExaminations() {
        Nurse nurse = nurseService.getLoginNurse();
        if (nurse == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            List<ExaminationForWorkCalendarDTO> examinationForWorkCalendarDTOS = convertToDTO(examinationService.getNurseExaminations(nurse.getId()));
            return new ResponseEntity<>(examinationForWorkCalendarDTOS, HttpStatus.OK);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/get-predefined-examinations")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<ExaminationPagingDTO> getPredefinedExaminations(Pageable page) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            return new ResponseEntity<>(examinationService.getPredefinedExaminations(clinicAdministrator, page), HttpStatus.OK);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private List<ExaminationForWorkCalendarDTO> convertToDTO(List<Examination> examinations) {
        List<ExaminationForWorkCalendarDTO> examinationForWorkCalendarDTOS = new ArrayList<>();
        for (Examination examination : examinations) {
            examinationForWorkCalendarDTOS.add(new ExaminationForWorkCalendarDTO(examination));
        }

        return examinationForWorkCalendarDTOS;
    }

    @PostMapping(path = "/predefined-examination", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<ExaminationDTO> createPredefinedExamination(@Valid @RequestBody PredefinedExaminationDTO predefinedExaminationDTO) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ExaminationDTO createdExamination = examinationService.createPredefinedExamination(predefinedExaminationDTO, clinicAdministrator);
        if (createdExamination == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(createdExamination, HttpStatus.CREATED);
    }

    @PostMapping(path = "/create-examination-operation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ExaminationDTO> createExaminationOrOperation(@Valid @RequestBody CreateExaminationOrOperationDTO createExaminationOrOperationDTO) {
        Doctor doctor = doctorService.getLoginDoctor();
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        LocalDateTime examinationTime = LocalDateTime.now();
        Examination ongoingExamination = examinationService.getOngoingExamination(createExaminationOrOperationDTO.getPatient().getId(), doctor.getId(), examinationTime);
        if (ongoingExamination == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ExaminationDTO createdExamination = examinationService.createExaminationOrOperation(createExaminationOrOperationDTO, doctor);
        if (createdExamination == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(createdExamination, HttpStatus.CREATED);
    }
}
