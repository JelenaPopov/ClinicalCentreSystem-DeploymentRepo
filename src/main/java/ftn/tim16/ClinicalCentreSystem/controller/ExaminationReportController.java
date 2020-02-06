package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ExaminationReportDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.ExaminationReportForTableDTO;
import ftn.tim16.ClinicalCentreSystem.model.*;
import ftn.tim16.ClinicalCentreSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/examination-report", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExaminationReportController {

    @Autowired
    private ExaminationReportService examinationReportService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ExaminationService examinationService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private NurseService nurseService;

    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ExaminationReportDTO> create(@PathVariable("id") Long examinationId, @Valid @RequestBody ExaminationReportDTO examinationReportDTO) {
        Doctor doctor = doctorService.getLoginDoctor();
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Examination examination = examinationService.getExamination(examinationId);
        if (examination == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LocalDateTime examinationTime = LocalDateTime.now();
        Examination ongoingExamination = examinationService.getOngoingExamination(examination.getPatient().getId(), doctor.getId(), examinationTime);
        if (ongoingExamination == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ExaminationReport existingExaminationReport = examinationReportService.findByExaminationId(ongoingExamination.getId());
        if (existingExaminationReport != null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        ExaminationReportDTO createdExaminationReportDTO = examinationReportService.create(doctor, ongoingExamination, examinationReportDTO);
        if (createdExaminationReportDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(createdExaminationReportDTO, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ExaminationReportDTO> edit(@PathVariable("id") Long examinationId, @Valid @RequestBody ExaminationReportDTO examinationReportDTO) {
        Doctor doctor = doctorService.getLoginDoctor();
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Examination examination = examinationService.getExamination(examinationId);
        if (examination == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LocalDateTime examinationTime = LocalDateTime.now();
        Examination ongoingExamination = examinationService.getOngoingExamination(examination.getPatient().getId(), doctor.getId(), examinationTime);
        if (ongoingExamination == null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        ExaminationReportDTO editedExaminationReportDTO = examinationReportService.edit(doctor, examinationReportDTO);
        if (editedExaminationReportDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(editedExaminationReportDTO, HttpStatus.CREATED);
    }

    @GetMapping(value = "/patients-all/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public ResponseEntity<List<ExaminationReportForTableDTO>> getPatientExaminationReports(@PathVariable("id") Long patientId) {
        Patient patient = patientService.getPatient(patientId);
        if (patient == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Doctor doctor = doctorService.getLoginDoctor();
        if (doctor != null && !examinationService.hasDoctorHeldExaminationForPatient(doctor, patient)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (doctor == null) {
            Nurse nurse = nurseService.getLoginNurse();
            if (nurse == null) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else if (!examinationService.hasNurseHeldExaminationForPatient(nurse, patient)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<>(examinationReportService.getPatientExaminationReports(patientId), HttpStatus.OK);
    }
}
