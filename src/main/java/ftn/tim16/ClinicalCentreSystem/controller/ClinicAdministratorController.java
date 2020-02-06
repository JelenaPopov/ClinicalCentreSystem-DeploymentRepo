package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ClinicAdministratorDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.EditClinicAdminDTO;
import ftn.tim16.ClinicalCentreSystem.service.ClinicAdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/clinic-administrator", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClinicAdministratorController {

    @Autowired
    private ClinicAdministratorService clinicAdministratorService;


    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('CLINICAL_CENTRE_ADMIN')")
    public ResponseEntity<List<ClinicAdministratorDTO>> getAllClinicAdministratorsInClinic(@RequestParam Long clinicId) {
        return new ResponseEntity<>(clinicAdministratorService.getAllClinicAdministratorsInClinic(clinicId), HttpStatus.OK);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINICAL_CENTRE_ADMIN')")
    public ResponseEntity<ClinicAdministratorDTO> addClinicAdministrator(@Valid @RequestBody ClinicAdministratorDTO clinicAdministratorDTO) {
        ClinicAdministratorDTO clinicAdministrator = clinicAdministratorService.create(clinicAdministratorDTO);
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(clinicAdministrator, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public EditClinicAdminDTO getClinicAdmin(@PathVariable Long id) {

        return clinicAdministratorService.findClinicAdminById(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<ClinicAdministratorDTO> editPersonalInformation(@Valid @RequestBody EditClinicAdminDTO clinicAdministratorDTO) {
        ClinicAdministratorDTO clinicAdministrator = clinicAdministratorService.editPersonalInformation(clinicAdministratorDTO);
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(clinicAdministrator, HttpStatus.CREATED);
    }

}
