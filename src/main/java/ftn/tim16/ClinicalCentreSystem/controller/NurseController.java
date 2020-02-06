package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.EditNurseDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.NurseDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.NursesPagingDTO;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;
import ftn.tim16.ClinicalCentreSystem.service.ClinicAdministratorService;
import ftn.tim16.ClinicalCentreSystem.service.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/nurse", produces = MediaType.APPLICATION_JSON_VALUE)
public class NurseController {

    @Autowired
    private NurseService nurseService;

    @Autowired
    private ClinicAdministratorService clinicAdministratorService;

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<List<NurseDTO>> getAllNursesInClinic() {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(nurseService.getAllNursesInClinic(clinicAdministrator.getClinic().getId()), HttpStatus.OK);
    }

    @GetMapping(value = "/page-all")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<NursesPagingDTO> getAllNursesInClinic(Pageable page) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        NursesPagingDTO nursesPagingDTO = new NursesPagingDTO(
                nurseService.getAllNursesInClinic(clinicAdministrator.getClinic().getId(), page),
                nurseService.getAllNursesInClinic(clinicAdministrator.getClinic().getId()).size()
        );

        return new ResponseEntity<>(nursesPagingDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<NurseDTO> addNurse(@Valid @RequestBody NurseDTO nurseDTO) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            NurseDTO nurse = nurseService.create(nurseDTO, clinicAdministrator);
            if (nurse == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(nurse, HttpStatus.CREATED);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}")
    public EditNurseDTO getNurse(@PathVariable Long id) {

        return nurseService.findNurseById(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<NurseDTO> editPersonalInformation(@Valid @RequestBody EditNurseDTO doctorDTO) {
        NurseDTO nurse = nurseService.editPersonalInformation(doctorDTO);
        if (nurse == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(nurse, HttpStatus.CREATED);
    }
}
