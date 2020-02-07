package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.request.AssignExaminationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.request.CreateRoomDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.RoomDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.RoomWithIdDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.RoomPagingDTO;
import ftn.tim16.ClinicalCentreSystem.model.Clinic;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;
import ftn.tim16.ClinicalCentreSystem.model.Room;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomService {

    Room findById(Long id);

    RoomWithIdDTO create(CreateRoomDTO roomDTO, ClinicAdministrator clinicAdministrator);

    RoomWithIdDTO edit(RoomWithIdDTO roomDTO, Long clinicId) throws Exception;

    RoomWithIdDTO assignRoom(AssignExaminationDTO examination, ClinicAdministrator clinicAdministrator);

    void automaticallyAssignRoom();

    List<RoomDTO> findAllRoomsInClinic(Clinic clinic);

    RoomPagingDTO searchRoomsInClinic(String kind, Clinic clinic, Pageable page, String search, String date, String searchStartTime, String searchEndTime);

    List<RoomDTO> getAvailableExaminationRooms(Long clinicId, String startDateTime, String endDateTime);

    RoomWithIdDTO deleteRoom(Long clinicId, Long roomId);

    boolean isAvailable(Room currentRoom, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
