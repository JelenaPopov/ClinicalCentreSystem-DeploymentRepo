package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ClinicalCentreAdminDTO;
import ftn.tim16.ClinicalCentreSystem.model.ClinicalCentreAdministrator;
import ftn.tim16.ClinicalCentreSystem.service.ClinicalCentreAdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/clinical-centre-admin")
public class ClinicalCentreAdministratorController {

    @Autowired
    private ClinicalCentreAdministratorService clinicalCentreAdministratorService;

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('CLINICAL_CENTRE_ADMIN')")
    public ResponseEntity<List<ClinicalCentreAdminDTO>> getAllClinicalCentreAdministrators() {
        ClinicalCentreAdministrator clinicalCentreAdministrator = clinicalCentreAdministratorService.getLoginAdmin();
        if (clinicalCentreAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(clinicalCentreAdministratorService.getAllClinicalCentreAdministrators(clinicalCentreAdministrator.getId()), HttpStatus.OK);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINICAL_CENTRE_ADMIN')")
    public ResponseEntity<ClinicalCentreAdminDTO> addClinicalCentreAdministrator(@Valid @RequestBody ClinicalCentreAdminDTO clinicalCentreAdminDTO) {
        ClinicalCentreAdminDTO clinicalCentreAdministrator = clinicalCentreAdministratorService.create(clinicalCentreAdminDTO);
        if (clinicalCentreAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(clinicalCentreAdministrator, HttpStatus.CREATED);
    }
}
