package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.MedicineDTO;
import ftn.tim16.ClinicalCentreSystem.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/medicine", produces = MediaType.APPLICATION_JSON_VALUE)
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINICAL_CENTRE_ADMIN')")
    public ResponseEntity<MedicineDTO> addMedicine(@Valid @RequestBody MedicineDTO medicine) {
        MedicineDTO createdMedicine = medicineService.create(medicine);
        if (createdMedicine == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(createdMedicine, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasAnyRole('CLINICAL_CENTRE_ADMIN', 'DOCTOR')")
    public ResponseEntity<List<MedicineDTO>> getAllMedicines() {
        return new ResponseEntity<>(medicineService.findAll(), HttpStatus.OK);
    }

}
