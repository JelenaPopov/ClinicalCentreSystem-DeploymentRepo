package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateExaminationTypeDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ExaminationTypeDTO;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;
import ftn.tim16.ClinicalCentreSystem.model.Doctor;
import ftn.tim16.ClinicalCentreSystem.service.ClinicAdministratorService;
import ftn.tim16.ClinicalCentreSystem.service.DoctorService;
import ftn.tim16.ClinicalCentreSystem.service.ExaminationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/examination-type")
public class ExaminationTypeController {

    @Autowired
    private ExaminationTypeService examinationTypeService;

    @Autowired
    private ClinicAdministratorService clinicAdministratorService;

    @Autowired
    private DoctorService doctorService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<ExaminationTypeDTO> create(@Valid @RequestBody CreateExaminationTypeDTO examinationType) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ExaminationTypeDTO createdExaminationType = examinationTypeService.create(examinationType, clinicAdministrator.getClinic());
        if (createdExaminationType == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdExaminationType, HttpStatus.CREATED);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<ExaminationTypeDTO> edit(@Valid @RequestBody ExaminationTypeDTO examinationType) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            ExaminationTypeDTO changedExaminationType = examinationTypeService.edit(examinationType, clinicAdministrator.getClinic().getId());
            if (changedExaminationType == null) {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<>(changedExaminationType, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @PutMapping(value = "/edit-price-list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<ExaminationTypeDTO> editPriceList(@Valid @RequestBody ExaminationTypeDTO examinationType) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            ExaminationTypeDTO changedExaminationType = examinationTypeService.editPriceList(examinationType, clinicAdministrator.getClinic().getId());
            if (changedExaminationType == null) {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<>(changedExaminationType, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasAnyRole('CLINIC_ADMIN','DOCTOR')")
    public ResponseEntity<List<ExaminationTypeDTO>> getAllExaminationTypesForAdmin() {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            Doctor doctor = doctorService.getLoginDoctor();
            if (doctor == null) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(examinationTypeService.findAllTypesInClinic(doctor.getClinic().getId()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(examinationTypeService.findAllTypesInClinic(clinicAdministrator.getClinic().getId()), HttpStatus.OK);
        }

    }

    @GetMapping(value = "/search-types")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<List<ExaminationTypeDTO>> getAllExaminationTypesForAdmin(@RequestParam(value = "searchLabel") String searchLabel,
                                                                                   @RequestParam(value = "searchPrice") String searchPrice) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Double price;
        try {
            price = Double.parseDouble(searchPrice);
        } catch (Exception e) {
            price = null;
        }
        return new ResponseEntity<>(examinationTypeService.searchTypesInClinic(clinicAdministrator.getClinic(), searchLabel, price), HttpStatus.OK);
    }


    @GetMapping(value = "/by-clinic-id/{id}")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR')")
    public ResponseEntity<List<ExaminationTypeDTO>> findAllExaminationTypesByClinicId(@PathVariable("id") Long clinicId) {
        return new ResponseEntity<>(examinationTypeService.findAllTypesInClinic(clinicId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<ExaminationTypeDTO> deleteExaminationType(@PathVariable("id") Long id) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            ExaminationTypeDTO examinationType = examinationTypeService.deleteExaminationType(clinicAdministrator.getClinic().getId(), id);
            if (examinationType == null) {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<>(examinationType, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

}
