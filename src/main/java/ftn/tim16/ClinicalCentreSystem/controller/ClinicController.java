package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ClinicDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.EditClinicDTO;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;
import ftn.tim16.ClinicalCentreSystem.service.ClinicAdministratorService;
import ftn.tim16.ClinicalCentreSystem.service.ClinicService;
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
@RequestMapping(value = "/api/clinic")
public class ClinicController {

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private ClinicAdministratorService clinicAdministratorService;

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('CLINICAL_CENTRE_ADMIN','PATIENT','CLINIC_ADMIN')")
    public ResponseEntity<ClinicDTO> getClinic(@PathVariable Long id) {
        ClinicDTO clinicDTO = clinicService.findById(id);
        if (clinicDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(clinicDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINICAL_CENTRE_ADMIN')")
    public ResponseEntity<ClinicDTO> addClinic(@RequestBody ClinicDTO clinicDTO) {
        ClinicDTO clinic = clinicService.create(clinicDTO);
        if (clinic == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(clinic, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('CLINICAL_CENTRE_ADMIN')")
    public ResponseEntity<List<ClinicDTO>> getAllClinics() {
        List<ClinicDTO> allClinics = clinicService.findAll();
        return new ResponseEntity<>(allClinics, HttpStatus.OK);
    }

    @GetMapping(value = "/clinic-in-which-admin-works")
    @PreAuthorize("hasAnyRole('CLINICAL_CENTRE_ADMIN','PATIENT','CLINIC_ADMIN')")
    public ResponseEntity<ClinicDTO> getClinicInWhichClinicAdminWorks() {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new ClinicDTO(clinicAdministrator.getClinic()), HttpStatus.OK);
    }

    @GetMapping(value = "/revenue")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<Integer> getClinicRevenue(@RequestParam(value = "startDate", required = true) String startDateTime,
                                                    @RequestParam(value = "endDate", required = true) String endDateTime) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            Integer revenue = clinicService.getClinicRevenue(clinicAdministrator.getClinic().getId(), startDateTime, endDateTime);

            return new ResponseEntity<>(revenue, HttpStatus.OK);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/clinic-rating")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<Double> getClinicRating() {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Double clinicRating = clinicAdministrator.getClinic().getClinicRating();
        if (clinicRating < 0 || clinicRating > 5) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(clinicRating, HttpStatus.OK);
    }

    @GetMapping(value = "/daily-statistic")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<int[]> getDailyStatistic() {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        int[] dailyStatistic = clinicService.getDailyStatistic(clinicAdministrator.getClinic().getId());
        if (dailyStatistic == null || dailyStatistic.length != 7) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(dailyStatistic, HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<EditClinicDTO> edit(@Valid @RequestBody EditClinicDTO clinicDTO) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            EditClinicDTO changedClinic = clinicService.edit(clinicDTO, clinicAdministrator.getClinic().getId());
            if (changedClinic == null) {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<>(changedClinic, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/week-statistic")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<int[]> getWeekStatistic() {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        int[] statistic = clinicService.getWeekStatistic(clinicAdministrator.getClinic().getId());
        if (statistic == null || statistic.length != 4) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }

    @GetMapping(value = "/month-statistic")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<int[]> getMonthStatistic() {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        int[] statistic = clinicService.getMonthStatistic(clinicAdministrator.getClinic().getId());
        if (statistic == null || statistic.length != 12) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }
}
