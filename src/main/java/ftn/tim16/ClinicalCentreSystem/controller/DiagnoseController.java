package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.DiagnoseDTO;
import ftn.tim16.ClinicalCentreSystem.service.DiagnoseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/diagnose", produces = MediaType.APPLICATION_JSON_VALUE)
public class DiagnoseController {

    @Autowired
    private DiagnoseService diagnoseService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINICAL_CENTRE_ADMIN')")
    public ResponseEntity<DiagnoseDTO> addDiagnose(@Valid @RequestBody DiagnoseDTO diagnose) {
        DiagnoseDTO createdDiagnose = diagnoseService.create(diagnose);
        if (createdDiagnose == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(createdDiagnose, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasAnyRole('CLINICAL_CENTRE_ADMIN', 'DOCTOR')")
    public ResponseEntity<List<DiagnoseDTO>> getAllDiagnosis() {
        return new ResponseEntity<>(diagnoseService.findAll(), HttpStatus.OK);
    }

}
