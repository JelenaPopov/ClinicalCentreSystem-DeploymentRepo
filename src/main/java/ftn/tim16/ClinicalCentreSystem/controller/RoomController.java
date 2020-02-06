package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.request.AssignExaminationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.request.CreateRoomDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.RoomDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.RoomWithIdDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.RoomPagingDTO;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;
import ftn.tim16.ClinicalCentreSystem.service.ClinicAdministratorService;
import ftn.tim16.ClinicalCentreSystem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private ClinicAdministratorService clinicAdministratorService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<RoomWithIdDTO> create(@Valid @RequestBody CreateRoomDTO roomDTO) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();

        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        RoomWithIdDTO createdRoom = roomService.create(roomDTO, clinicAdministrator);
        if (createdRoom == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<RoomWithIdDTO> edit(@Valid @RequestBody RoomWithIdDTO roomDTO) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            RoomWithIdDTO changedRoom = roomService.edit(roomDTO, clinicAdministrator.getClinic().getId());
            if (changedRoom == null) {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity<>(changedRoom, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<List<RoomDTO>> getAllRoomsForAdmin() {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(roomService.findAllRoomsInClinic(clinicAdministrator.getClinic()), HttpStatus.OK);
    }

    @GetMapping(value = "/pageAll")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<RoomPagingDTO> searchRoomsInClinic(@RequestParam(value = "kind", required = true) String kind,
                                                             @RequestParam(value = "searchLabel") String searchLabel,
                                                             @RequestParam(value = "searchDate") String searchDate,
                                                             @RequestParam(value = "searchStartTime") String searchStartTime,
                                                             @RequestParam(value = "searchEndTime") String searchEndTime,
                                                             Pageable page) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            RoomPagingDTO roomPagingDTO = roomService.
                    searchRoomsInClinic(kind, clinicAdministrator.getClinic(), page, searchLabel, searchDate, searchStartTime, searchEndTime);
            if (roomPagingDTO == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(roomPagingDTO, HttpStatus.OK);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/available-examination-rooms")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<List<RoomDTO>> getAvailableExaminationRooms(@RequestParam(value = "startDateTime", required = true) String startDateTime,
                                                                      @RequestParam(value = "endDateTime", required = true) String endDateTime) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            List<RoomDTO> roomDTOS = roomService.getAvailableExaminationRooms(clinicAdministrator.getClinic().getId(), startDateTime, endDateTime);
            return new ResponseEntity<>(roomDTOS, HttpStatus.OK);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/assign", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<RoomWithIdDTO> assignRoom(@Valid @RequestBody AssignExaminationDTO examination) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();

        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        RoomWithIdDTO assignedRoom = roomService.assignRoom(examination, clinicAdministrator);
        if (assignedRoom == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(assignedRoom, HttpStatus.OK);
    }

    @Scheduled(cron = "${room.cron}")
    public void assignRoom() {
        roomService.automaticallyAssignRoom();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<RoomWithIdDTO> deleteRoom(@PathVariable("id") Long id) {
        ClinicAdministrator clinicAdministrator = clinicAdministratorService.getLoginAdmin();
        if (clinicAdministrator == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        RoomWithIdDTO room = roomService.deleteRoom(clinicAdministrator.getClinic().getId(), id);
        if (room == null) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(room, HttpStatus.ACCEPTED);

    }

}
