package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.model.Room;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static ftn.tim16.ClinicalCentreSystem.constants.RoomConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class RoomRepositoryUnitTests {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void testFindByClinicIdAndStatusAndLabelContainsIgnoringCase_withPaging() {
        Page<Room> rooms = roomRepository.findByClinicIdAndStatusAndLabelContainsIgnoringCase(CLINIC_ID,
                LOGICAL_STATUS_EXISTING, "", PAGE);
        assertEquals(DB_ROOM_WITH_STATUS_EXISTING_AND_CLINIC_ID, rooms.getContent().size());
        for (Room room : rooms) {
            assertEquals(LOGICAL_STATUS_EXISTING, room.getStatus());
            assertEquals(CLINIC_ID, room.getClinic().getId());
        }
    }

    @Test
    public void testFindByClinicIdAndStatusAndLabelContainsIgnoringCase_withPagingAndSearch() {
        Page<Room> rooms = roomRepository.findByClinicIdAndStatusAndLabelContainsIgnoringCase(CLINIC_ID,
                LOGICAL_STATUS_EXISTING, SEARCH, PAGE);
        assertEquals(DB_ROOM_WITH_STATUS_EXISTING_AND_CLINIC_ID_WITH_SEARCH, rooms.getContent().size());
        for (Room room : rooms) {
            assertEquals(LOGICAL_STATUS_EXISTING, room.getStatus());
            assertEquals(CLINIC_ID, room.getClinic().getId());
        }
    }

    @Test
    public void testFindByClinicIdAndStatusAndLabelContainsIgnoringCase() {
        List<Room> rooms = roomRepository.findByClinicIdAndStatusAndLabelContainsIgnoringCase(CLINIC_ID,
                LOGICAL_STATUS_EXISTING, "");
        assertEquals(DB_ROOM_WITH_STATUS_EXISTING_AND_CLINIC_ID, rooms.size());
        for (Room room : rooms) {
            assertEquals(LOGICAL_STATUS_EXISTING, room.getStatus());
            assertEquals(CLINIC_ID, room.getClinic().getId());
        }
    }

    @Test
    public void testFindByClinicIdAndStatusAndLabelContainsIgnoringCase_withSearch() {
        List<Room> rooms = roomRepository.findByClinicIdAndStatusAndLabelContainsIgnoringCase(CLINIC_ID,
                LOGICAL_STATUS_EXISTING, SEARCH);
        assertEquals(DB_ROOM_WITH_STATUS_EXISTING_AND_CLINIC_ID_WITH_SEARCH, rooms.size());
        for (Room room : rooms) {
            assertEquals(LOGICAL_STATUS_EXISTING, room.getStatus());
            assertEquals(CLINIC_ID, room.getClinic().getId());
        }
    }

    @Test
    public void testFindByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind_withPaging_returnExaminationRooms() {
        Page<Room> rooms = roomRepository.findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                ("", CLINIC_ID, LOGICAL_STATUS_EXISTING, EXAMINATION, PAGE);
        assertEquals(DB_EXAMINATION_ROOMS, rooms.getContent().size());
        for (Room room : rooms) {
            assertEquals(LOGICAL_STATUS_EXISTING, room.getStatus());
            assertEquals(CLINIC_ID, room.getClinic().getId());
            assertEquals(EXAMINATION, room.getKind());
        }
    }

    @Test
    public void testFindByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind_withPaging_returnOperationRooms() {
        Page<Room> rooms = roomRepository.findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                ("", CLINIC_ID, LOGICAL_STATUS_EXISTING, OPERATION, PAGE);
        assertEquals(DB_OPERATION_ROOMS, rooms.getContent().size());
        for (Room room : rooms) {
            assertEquals(LOGICAL_STATUS_EXISTING, room.getStatus());
            assertEquals(CLINIC_ID, room.getClinic().getId());
            assertEquals(OPERATION, room.getKind());
        }
    }

    @Test
    public void testFindByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind_returnExaminationRooms() {
        List<Room> rooms = roomRepository.findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                (SEARCH_ROOM, CLINIC_ID, LOGICAL_STATUS_EXISTING, EXAMINATION);
        assertEquals(DB_EXAMINATION_ROOMS, rooms.size());
        for (Room room : rooms) {
            assertEquals(LOGICAL_STATUS_EXISTING, room.getStatus());
            assertEquals(CLINIC_ID, room.getClinic().getId());
            assertEquals(EXAMINATION, room.getKind());
        }
    }

    @Test
    public void testFindByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind_returnOperationRooms() {
        List<Room> rooms = roomRepository.findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                (SEARCH_ROOM, CLINIC_ID, LOGICAL_STATUS_EXISTING, OPERATION);
        assertEquals(DB_OPERATION_ROOMS, rooms.size());
        for (Room room : rooms) {
            assertEquals(LOGICAL_STATUS_EXISTING, room.getStatus());
            assertEquals(CLINIC_ID, room.getClinic().getId());
            assertEquals(OPERATION, room.getKind());
        }
    }

    @Test
    public void testGetByIdAndStatusNot_exist() {
        Room room = roomRepository.getByIdAndStatusNot(ROOM_1_ID, LOGICAL_STATUS_DELETED);

        assertNotNull(room);
        assertEquals(ROOM_1_ID, room.getId());
    }

    @Test
    public void testGetByIdAndStatusNot_doesntExist() {
        Room room = roomRepository.getByIdAndStatusNot(ROOM_4_ID, LOGICAL_STATUS_DELETED);

        assertNull(room);
    }

    @Test
    public void testGetByIdAndStatus_exist() {
        Room room = roomRepository.getByIdAndStatus(ROOM_1_ID, LOGICAL_STATUS_EXISTING);

        assertNotNull(room);
        assertEquals(ROOM_1_ID, room.getId());
        assertEquals(LOGICAL_STATUS_EXISTING, room.getStatus());
    }

    @Test
    public void testGetByIdAndStatus_doesntExist() {
        Room room = roomRepository.getByIdAndStatus(ROOM_4_ID, LOGICAL_STATUS_EXISTING);

        assertNull(room);
    }
}
