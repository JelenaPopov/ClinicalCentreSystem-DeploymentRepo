package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.request.AssignExaminationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.RoomDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.RoomWithIdDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.RoomPagingDTO;
import ftn.tim16.ClinicalCentreSystem.model.*;
import ftn.tim16.ClinicalCentreSystem.repository.RoomRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ftn.tim16.ClinicalCentreSystem.constants.RoomConstants.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class RoomServiceUnitTests {

    @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepositoryMocked;

    @MockBean
    private ExaminationService examinationServiceMocked;

    @MockBean
    private DoctorService doctorServiceMocked;

    @MockBean
    private NurseService nurseServiceMocked;

    @MockBean
    private EmailNotificationService emailNotificationServiceMocked;

    @MockBean
    private DateTimeIntervalService dateTimeIntervalServiceMocked;

    @Test
    public void testSearchRoomsInClinic_returnExaminationAndOperationRooms() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        Room room2 = new Room(ROOM_LABEL_2, OPERATION, clinic);
        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);

        Page<Room> roomPage = new PageImpl(rooms);
        List<RoomDTO> roomDTOS = new ArrayList<>();
        for (Room currentRoom : rooms) {
            roomDTOS.add(new RoomDTO(currentRoom));
        }
        Mockito.when(roomRepositoryMocked.findByClinicIdAndStatusAndLabelContainsIgnoringCase(CLINIC_ID,
                LOGICAL_STATUS_EXISTING, SEARCH, PAGE)).thenReturn(roomPage);
        Mockito.when(roomRepositoryMocked.findByClinicIdAndStatusAndLabelContainsIgnoringCase(CLINIC_ID,
                LOGICAL_STATUS_EXISTING, SEARCH)).thenReturn(rooms);
        RoomPagingDTO roomPagingDTO = new RoomPagingDTO(roomDTOS, rooms.size());

        RoomPagingDTO roomPagingDTOResults = roomService.searchRoomsInClinic(ALL_ROOMS,
                clinic, PAGE, SEARCH, EMPTY_DATE, EMPTY_SEARCH_START_TIME, EMPTY_SEARCH_END_TIME);
        Assert.assertNotNull(roomPagingDTOResults);
        Assert.assertEquals(roomPagingDTO.getRoomDTOList().size(), roomPagingDTOResults.getRoomDTOList().size());
        Assert.assertEquals(roomPagingDTO.getNumberOfItems(), roomPagingDTOResults.getNumberOfItems());
        verify(roomRepositoryMocked, times(1)).findByClinicIdAndStatusAndLabelContainsIgnoringCase(CLINIC_ID,
                LOGICAL_STATUS_EXISTING, SEARCH, PAGE);
        verify(roomRepositoryMocked, times(1)).findByClinicIdAndStatusAndLabelContainsIgnoringCase(CLINIC_ID,
                LOGICAL_STATUS_EXISTING, SEARCH);
    }

    @Test
    public void testSearchRoomsInClinic_returnExaminationRooms() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        Room room2 = new Room(ROOM_LABEL_2, EXAMINATION, clinic);
        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);

        Page<Room> roomPage = new PageImpl(rooms);
        List<RoomDTO> roomDTOS = new ArrayList<>();
        for (Room currentRoom : rooms) {
            roomDTOS.add(new RoomDTO(currentRoom));
        }

        Mockito.when(roomRepositoryMocked.findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                (SEARCH, CLINIC_ID, LOGICAL_STATUS_EXISTING, EXAMINATION)).thenReturn(rooms);

        Mockito.when(roomRepositoryMocked.findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                (SEARCH, CLINIC_ID, LOGICAL_STATUS_EXISTING, EXAMINATION, PAGE)).thenReturn(roomPage);

        RoomPagingDTO roomPagingDTO = new RoomPagingDTO(roomDTOS, rooms.size());

        RoomPagingDTO roomPagingDTOResults = roomService.searchRoomsInClinic(EXAMINATION_ROOMS,
                clinic, PAGE, SEARCH, EMPTY_DATE, EMPTY_SEARCH_START_TIME, EMPTY_SEARCH_END_TIME);

        Assert.assertNotNull(roomPagingDTOResults);
        Assert.assertEquals(roomPagingDTO.getRoomDTOList().size(), roomPagingDTOResults.getRoomDTOList().size());
        Assert.assertEquals(roomPagingDTO.getNumberOfItems(), roomPagingDTOResults.getNumberOfItems());
        for (RoomDTO roomDTO : roomPagingDTOResults.getRoomDTOList()) {
            Assert.assertEquals(EXAMINATION_ROOMS, roomDTO.getKind());
        }
        verify(roomRepositoryMocked, times(1)).findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                (SEARCH, CLINIC_ID, LOGICAL_STATUS_EXISTING, EXAMINATION);
        verify(roomRepositoryMocked, times(1)).findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                (SEARCH, CLINIC_ID, LOGICAL_STATUS_EXISTING, EXAMINATION, PAGE);
    }

    @Test
    public void testSearchRoomsInClinic_returnAvailableExaminationRooms() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        room1.setId(ROOM_1_ID);

        Room room2 = new Room(ROOM_LABEL_2, EXAMINATION, clinic);
        room2.setId(ROOM_2_ID);

        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);

        List<RoomDTO> roomDTOS = new ArrayList<>();
        for (Room currentRoom : rooms) {
            roomDTOS.add(new RoomDTO(currentRoom));
        }
        LocalDateTime startDateTime = getLocalDateTime(getDate(DATE), SEARCH_START_TIME);

        Mockito.when(roomRepositoryMocked.findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                (SEARCH, CLINIC_ID, LOGICAL_STATUS_EXISTING, EXAMINATION)).thenReturn(rooms);
        Mockito.when(examinationServiceMocked.getExaminationsOnDay
                (ROOM_1_ID, startDateTime)).thenReturn(new ArrayList<>());
        Mockito.when(examinationServiceMocked.getExaminationsOnDay
                (ROOM_2_ID, startDateTime)).thenReturn(new ArrayList<>());

        RoomPagingDTO roomPagingDTO = new RoomPagingDTO(roomDTOS, rooms.size());

        RoomPagingDTO roomPagingDTOResults = roomService.searchRoomsInClinic(EXAMINATION_ROOMS,
                clinic, PAGE, SEARCH, DATE, SEARCH_START_TIME, SEARCH_END_TIME);

        Assert.assertNotNull(roomPagingDTOResults);
        Assert.assertEquals(roomPagingDTO.getRoomDTOList().size(), roomPagingDTOResults.getRoomDTOList().size());
        Assert.assertEquals(roomPagingDTO.getNumberOfItems(), roomPagingDTOResults.getNumberOfItems());
        for (RoomDTO roomDTO : roomPagingDTOResults.getRoomDTOList()) {
            Assert.assertEquals(EXAMINATION_ROOMS, roomDTO.getKind());
        }
        verify(roomRepositoryMocked, times(1)).findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                (SEARCH, CLINIC_ID, LOGICAL_STATUS_EXISTING, EXAMINATION);

        verify(examinationServiceMocked, times(1)).getExaminationsOnDay
                (ROOM_1_ID, startDateTime);

        verify(examinationServiceMocked, times(1)).getExaminationsOnDay
                (ROOM_2_ID, startDateTime);
    }


    @Test
    public void testSearchRoomsInClinic_returnAvailableExaminationRoomsWhichHaveExaminations() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);
        Room room2 = new Room(ROOM_LABEL_2, EXAMINATION, clinic);
        room2.setId(ROOM_2_ID);

        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        room1.setId(ROOM_1_ID);
        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_APPROVED, null, room1, DISCOUNT, null, clinic, null);

        Set<Examination> examinations = new HashSet<>();
        List<Examination> examinationList = new ArrayList<>();
        examinations.add(examination);
        examinationList.add(examination);
        room1.setExaminations(examinations);

        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);

        List<RoomDTO> roomDTOS = new ArrayList<>();
        roomDTOS.add(new RoomDTO(room2));

        LocalDateTime startDateTime = getLocalDateTime(getDate(DATE), SEARCH_START_TIME);

        Mockito.when(roomRepositoryMocked.findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                (SEARCH, CLINIC_ID, LOGICAL_STATUS_EXISTING, EXAMINATION)).thenReturn(rooms);
        Mockito.when(examinationServiceMocked.getExaminationsOnDay
                (ROOM_1_ID, startDateTime)).thenReturn(examinationList);
        Mockito.when(examinationServiceMocked.getExaminationsOnDay
                (ROOM_2_ID, startDateTime)).thenReturn(new ArrayList<>());

        RoomPagingDTO roomPagingDTO = new RoomPagingDTO(roomDTOS, rooms.size() - 1);

        RoomPagingDTO roomPagingDTOResults = roomService.searchRoomsInClinic(EXAMINATION_ROOMS,
                clinic, PAGE, SEARCH, DATE, SEARCH_START_TIME, SEARCH_END_TIME);

        Assert.assertNotNull(roomPagingDTOResults);
        Assert.assertEquals(roomPagingDTO.getRoomDTOList().size(), roomPagingDTOResults.getRoomDTOList().size());
        Assert.assertEquals(roomPagingDTO.getNumberOfItems(), roomPagingDTOResults.getNumberOfItems());

        for (RoomDTO roomDTO : roomPagingDTOResults.getRoomDTOList()) {
            Assert.assertEquals(EXAMINATION_ROOMS, roomDTO.getKind());
        }
        verify(roomRepositoryMocked, times(1)).findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                (SEARCH, CLINIC_ID, LOGICAL_STATUS_EXISTING, EXAMINATION);

        verify(examinationServiceMocked, times(1)).getExaminationsOnDay
                (ROOM_1_ID, startDateTime);

        verify(examinationServiceMocked, times(1)).getExaminationsOnDay
                (ROOM_2_ID, startDateTime);
    }

    @Test
    public void testSearchRoomsInClinic_returnAvailableExaminationRoomsOnAnotherDay() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        room1.setId(ROOM_1_ID);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_APPROVED, null, room1, DISCOUNT, null, clinic, null);

        Set<Examination> examinations = new HashSet<>();
        List<Examination> examinationList = new ArrayList<>();
        examinations.add(examination);
        examinationList.add(examination);
        room1.setExaminations(examinations);

        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);

        List<RoomDTO> roomDTOS = new ArrayList<>();
        roomDTOS.add(new RoomDTO(room1));

        LocalDateTime startDateTime = getLocalDateTime(getDate(DATE), SEARCH_START_TIME);

        Mockito.when(roomRepositoryMocked.findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                (SEARCH, CLINIC_ID, LOGICAL_STATUS_EXISTING, EXAMINATION)).thenReturn(rooms);
        Mockito.when(examinationServiceMocked.getExaminationsOnDay
                (ROOM_1_ID, startDateTime)).thenReturn(examinationList);
        Mockito.when(examinationServiceMocked.getExaminationsAfter
                (ROOM_1_ID, startDateTime)).thenReturn(examinationList);

        RoomPagingDTO roomPagingDTO = new RoomPagingDTO(roomDTOS, rooms.size());

        RoomPagingDTO roomPagingDTOResults = roomService.searchRoomsInClinic(EXAMINATION_ROOMS,
                clinic, PAGE, SEARCH, DATE, SEARCH_START_TIME, SEARCH_END_TIME);

        Assert.assertNotNull(roomPagingDTOResults);
        Assert.assertEquals(roomPagingDTO.getRoomDTOList().size(), roomPagingDTOResults.getRoomDTOList().size());
        Assert.assertEquals(roomPagingDTO.getNumberOfItems(), roomPagingDTOResults.getNumberOfItems());

        for (RoomDTO roomDTO : roomPagingDTOResults.getRoomDTOList()) {
            Assert.assertEquals(EXAMINATION_ROOMS, roomDTO.getKind());
            Assert.assertEquals(endDate, roomDTO.getAvailable());
        }
        verify(roomRepositoryMocked, times(1)).findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind
                (SEARCH, CLINIC_ID, LOGICAL_STATUS_EXISTING, EXAMINATION);
        verify(examinationServiceMocked, times(1)).getExaminationsOnDay
                (ROOM_1_ID, startDateTime);
        verify(examinationServiceMocked, times(1)).getExaminationsAfter
                (ROOM_1_ID, startDateTime);

    }

    @Test
    public void testAssignExaminationRoom_roomDoesNotExist() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        room1.setId(ROOM_1_ID);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        examination.setId(EXAMINATION_ID);

        Mockito.when(examinationServiceMocked.getExamination(EXAMINATION_ID)).thenReturn(examination);

        Mockito.when(roomRepositoryMocked.getByIdAndStatusNot
                (ROOM_1_ID, LOGICAL_STATUS_DELETED)).thenReturn(null);

        AssignExaminationDTO assignExaminationDTO = new AssignExaminationDTO();
        assignExaminationDTO.setId(EXAMINATION_ID);
        assignExaminationDTO.setRoomId(ROOM_1_ID);
        assignExaminationDTO.setLabel(ROOM_LABEL_1);
        assignExaminationDTO.setKind(EXAMINATION_ROOMS);
        assignExaminationDTO.setAvailable(AVAILABLE_ROOM);

        RoomWithIdDTO roomWithIdDTOResult = roomService.assignRoom(assignExaminationDTO, getLoggedInClinicAdmin());

        Assert.assertNull(roomWithIdDTOResult);

        verify(examinationServiceMocked, times(2)).getExamination
                (EXAMINATION_ID);
        verify(roomRepositoryMocked, times(1)).getByIdAndStatusNot
                (ROOM_1_ID, LOGICAL_STATUS_DELETED);
    }

    @Test
    public void testAssignExaminationRoom_roomIsNotAvailable() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        room1.setId(ROOM_1_ID);

        LocalDateTime startDateTime = getLocalDateTime(getDate(DATE), SEARCH_START_TIME);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_APPROVED, null, room1, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        examination.setId(EXAMINATION_ID);
        List<Examination> examinationsList = new ArrayList<>();
        examinationsList.add(examination);

        Set<Examination> examinations = new HashSet<>();
        examinations.add(examination);
        room1.setExaminations(examinations);

        Examination examination2 = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        examination.setId(EXAMINATION_ID + 1);

        Mockito.when(examinationServiceMocked.getExamination(EXAMINATION_ID + 1)).thenReturn(examination2);

        Mockito.when(roomRepositoryMocked.getByIdAndStatusNot
                (ROOM_1_ID, LOGICAL_STATUS_DELETED)).thenReturn(room1);
        Mockito.when(examinationServiceMocked.getExaminationsOnDay
                (ROOM_1_ID, startDateTime)).thenReturn(examinationsList);

        AssignExaminationDTO assignExaminationDTO = new AssignExaminationDTO();
        assignExaminationDTO.setId(EXAMINATION_ID + 1);
        assignExaminationDTO.setRoomId(ROOM_1_ID);
        assignExaminationDTO.setLabel(ROOM_LABEL_1);
        assignExaminationDTO.setKind(EXAMINATION_ROOMS);
        assignExaminationDTO.setAvailable(AVAILABLE_ROOM);

        RoomWithIdDTO roomWithIdDTOResult = roomService.assignRoom(assignExaminationDTO, getLoggedInClinicAdmin());

        Assert.assertNull(roomWithIdDTOResult);

        verify(examinationServiceMocked, times(2)).getExamination
                (EXAMINATION_ID + 1);
        verify(roomRepositoryMocked, times(1)).getByIdAndStatusNot
                (ROOM_1_ID, LOGICAL_STATUS_DELETED);
        verify(examinationServiceMocked, times(1)).getExaminationsOnDay
                (ROOM_1_ID, startDateTime);
    }

    @Test
    public void testAssignExaminationRoom_error_nurseDoesNotExist() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        room1.setId(ROOM_1_ID);

        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);
        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_lAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        examination.setId(EXAMINATION_ID);

        Set<Doctor> doctors = new HashSet<Doctor>();
        doctors.add(doctor);
        examination.getDoctors().add(doctor);

        Mockito.when(examinationServiceMocked.getExamination(EXAMINATION_ID)).thenReturn(examination);
        Mockito.when(roomRepositoryMocked.getByIdAndStatusNot
                (ROOM_1_ID, LOGICAL_STATUS_DELETED)).thenReturn(room1);
        Mockito.when(examinationServiceMocked.getExaminationsOnDay
                (ROOM_1_ID, startDate)).thenReturn(new ArrayList<>());
        Mockito.when(nurseServiceMocked.getRandomNurse
                (CLINIC_ID, startDate, endDate)).thenReturn(null);

        AssignExaminationDTO assignExaminationDTO = new AssignExaminationDTO();
        assignExaminationDTO.setId(EXAMINATION_ID);
        assignExaminationDTO.setRoomId(ROOM_1_ID);
        assignExaminationDTO.setLabel(ROOM_LABEL_1);
        assignExaminationDTO.setKind(EXAMINATION_ROOMS);
        assignExaminationDTO.setAvailable(AVAILABLE_ROOM_08);

        RoomWithIdDTO roomWithIdDTOResult = roomService.assignRoom(assignExaminationDTO, getLoggedInClinicAdmin());

        Assert.assertNull(roomWithIdDTOResult);

        verify(examinationServiceMocked, times(2)).getExamination
                (EXAMINATION_ID);
        verify(roomRepositoryMocked, times(1)).getByIdAndStatusNot
                (ROOM_1_ID, LOGICAL_STATUS_DELETED);
        verify(examinationServiceMocked, times(1)).getExaminationsOnDay
                (ROOM_1_ID, startDate);
        verify(nurseServiceMocked, times(1)).getRandomNurse
                (CLINIC_ID, startDate, endDate);
    }

    @Test
    public void testAssignExaminationRoom_success() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);
        Nurse nurse = new Nurse(NEW_NURSE_EMAIL, NEW_NURSE_PASSWORD, NEW_NURSE_FIRST_NAME, NEW_NURSE_lAST_NAME, NEW_NURSE_PHONE_NUMBER,
                LocalTime.parse(NEW_NURSE_WORK_HOURS_FROM), LocalTime.parse(NEW_NURSE_WORK_HOURS_TO), clinic, getAuthority(NEW_NURSE_AUTHORITY));
        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        room1.setId(ROOM_1_ID);

        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);
        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_lAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));
        doctor.setId(DOCTOR_ID);
        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        examination.setId(EXAMINATION_ID);

        Set<Doctor> doctors = new HashSet<Doctor>();
        doctors.add(doctor);
        examination.getDoctors().add(doctor);

        Mockito.when(examinationServiceMocked.getExamination(EXAMINATION_ID)).thenReturn(examination);
        Mockito.when(roomRepositoryMocked.getByIdAndStatusNot
                (ROOM_1_ID, LOGICAL_STATUS_DELETED)).thenReturn(room1);
        Mockito.when(examinationServiceMocked.getExaminationsOnDay
                (ROOM_1_ID, startDate)).thenReturn(new ArrayList<>());
        Mockito.when(nurseServiceMocked.getRandomNurse
                (CLINIC_ID, startDate, endDate)).thenReturn(nurse);

        Examination examinationApproved = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        examinationApproved.setId(EXAMINATION_ID);
        examinationApproved.setStatus(APPROVED);
        Set<Examination> examinations = new HashSet<>();
        examinations.add(examinationApproved);
        room1.setExaminations(examinations);
        Mockito.when(examinationServiceMocked.assignRoom(examination, room1, nurse)).thenReturn(examinationApproved);
        Mockito.doNothing().when(emailNotificationServiceMocked).sendEmail(anyString(), anyString(), anyString());

        Mockito.when(roomRepositoryMocked.getByIdAndStatus(ROOM_1_ID, LOGICAL_STATUS_EXISTING)).thenReturn(room1);

        AssignExaminationDTO assignExaminationDTO = new AssignExaminationDTO();
        assignExaminationDTO.setId(EXAMINATION_ID);
        assignExaminationDTO.setRoomId(ROOM_1_ID);
        assignExaminationDTO.setLabel(ROOM_LABEL_1);
        assignExaminationDTO.setKind(EXAMINATION_ROOMS);
        assignExaminationDTO.setAvailable(AVAILABLE_ROOM_08);

        RoomWithIdDTO roomWithIdDTOResult = roomService.assignRoom(assignExaminationDTO, getLoggedInClinicAdmin());

        Assert.assertNotNull(roomWithIdDTOResult);
        Assert.assertEquals(room1.getId(), roomWithIdDTOResult.getId());

        verify(examinationServiceMocked, times(2)).getExamination
                (EXAMINATION_ID);
        verify(roomRepositoryMocked, times(1)).getByIdAndStatusNot
                (ROOM_1_ID, LOGICAL_STATUS_DELETED);
        verify(examinationServiceMocked, times(1)).getExaminationsOnDay
                (ROOM_1_ID, startDate);
        verify(nurseServiceMocked, times(1)).getRandomNurse
                (CLINIC_ID, startDate, endDate);
        verify(examinationServiceMocked, times(1)).assignRoom(examination, room1, nurse);
        verify(roomRepositoryMocked, times(1)).getByIdAndStatus(ROOM_1_ID, LOGICAL_STATUS_EXISTING);
    }

    @Test
    public void testAssignExaminationRoom_successChangeTime() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        DateTimeInterval dateTimeIntervalAvailableRoom = new DateTimeInterval(LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR_11, MIN, SEC),
                LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR_14, MIN, SEC));

        Nurse nurse = new Nurse(NEW_NURSE_EMAIL, NEW_NURSE_PASSWORD, NEW_NURSE_FIRST_NAME, NEW_NURSE_lAST_NAME, NEW_NURSE_PHONE_NUMBER,
                LocalTime.parse(NEW_NURSE_WORK_HOURS_FROM), LocalTime.parse(NEW_NURSE_WORK_HOURS_TO), clinic, getAuthority(NEW_NURSE_AUTHORITY));
        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        room1.setId(ROOM_1_ID);

        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);
        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_lAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));
        doctor.setId(DOCTOR_ID);
        Examination approvedExamination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_APPROVED, null, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        approvedExamination.setId(EXAMINATION_ID + 1);

        Set<Examination> examinationsList = new HashSet<>();
        List<Examination> approvedExaminations = new ArrayList<>();
        approvedExaminations.add(approvedExamination);
        examinationsList.add(approvedExamination);
        room1.setExaminations(examinationsList);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        examination.setId(EXAMINATION_ID);

        Set<Doctor> doctors = new HashSet<Doctor>();
        doctors.add(doctor);
        examination.getDoctors().add(doctor);

        Mockito.when(examinationServiceMocked.getExamination(EXAMINATION_ID)).thenReturn(examination);
        Mockito.when(roomRepositoryMocked.getByIdAndStatusNot
                (ROOM_1_ID, LOGICAL_STATUS_DELETED)).thenReturn(room1);
        Mockito.when(examinationServiceMocked.getExaminationsOnDay
                (ROOM_1_ID, startDate)).thenReturn(approvedExaminations);
        Mockito.when(dateTimeIntervalServiceMocked.create
                (LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR_11, MIN, SEC),
                        LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR_14, MIN, SEC))).thenReturn(dateTimeIntervalAvailableRoom);

        Mockito.when(nurseServiceMocked.getRandomNurse
                (CLINIC_ID, dateTimeIntervalAvailableRoom.getStartDateTime(), dateTimeIntervalAvailableRoom.getEndDateTime())).thenReturn(nurse);
        Mockito.when(doctorServiceMocked.haveToChangeDoctor
                (examination, doctor, dateTimeIntervalAvailableRoom.getStartDateTime(), dateTimeIntervalAvailableRoom.getEndDateTime())).thenReturn(true);

        Examination examinationApproved = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());

        examinationApproved.setId(EXAMINATION_ID);
        examinationApproved.setStatus(APPROVED);
        Set<Examination> examinations = new HashSet<>();
        examinations.add(examinationApproved);
        room1.setExaminations(examinations);
        Mockito.when(examinationServiceMocked.assignRoom(examination, room1, nurse)).thenReturn(examinationApproved);
        Mockito.doNothing().when(emailNotificationServiceMocked).sendEmail(anyString(), anyString(), anyString());

        Mockito.when(roomRepositoryMocked.getByIdAndStatus(ROOM_1_ID, LOGICAL_STATUS_EXISTING)).thenReturn(room1);

        AssignExaminationDTO assignExaminationDTO = new AssignExaminationDTO();
        assignExaminationDTO.setId(EXAMINATION_ID);
        assignExaminationDTO.setRoomId(ROOM_1_ID);
        assignExaminationDTO.setLabel(ROOM_LABEL_1);
        assignExaminationDTO.setKind(EXAMINATION_ROOMS);
        assignExaminationDTO.setAvailable(AVAILABLE_ROOM_11);

        RoomWithIdDTO roomWithIdDTOResult = roomService.assignRoom(assignExaminationDTO, getLoggedInClinicAdmin());

        Assert.assertNotNull(roomWithIdDTOResult);
        Assert.assertEquals(room1.getId(), roomWithIdDTOResult.getId());

        verify(examinationServiceMocked, times(2)).getExamination
                (EXAMINATION_ID);
        verify(roomRepositoryMocked, times(1)).getByIdAndStatusNot
                (ROOM_1_ID, LOGICAL_STATUS_DELETED);
        verify(nurseServiceMocked, times(1)).getRandomNurse
                (CLINIC_ID, dateTimeIntervalAvailableRoom.getStartDateTime(), dateTimeIntervalAvailableRoom.getEndDateTime());
        verify(examinationServiceMocked, times(1)).assignRoom(examination, room1, nurse);
        verify(roomRepositoryMocked, times(1)).getByIdAndStatus(ROOM_1_ID, LOGICAL_STATUS_EXISTING);
        verify(doctorServiceMocked, times(1)).
                haveToChangeDoctor(examination, doctor, dateTimeIntervalAvailableRoom.getStartDateTime(), dateTimeIntervalAvailableRoom.getEndDateTime());
    }

    private LocalDateTime getLocalDateTime(LocalDate date, String time) throws DateTimeParseException {
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        return LocalDateTime.of(date, localTime);
    }

    private LocalDate getDate(String date) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    private ClinicAdministrator getLoggedInClinicAdmin() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        ClinicAdministrator clinicAdministrator = new ClinicAdministrator(CLINIC_ADMIN_EMAIL, CLINIC_ADMIN_PASSWORD,
                CLINIC_ADMIN_FIRST_NAME, CLINIC_ADMIN_LAST_NAME, CLINIC_ADMIN_PHONE_NUMBER, clinic, getClinicAdminAuthority());
        clinicAdministrator.setId(CLINIC_ADMIN_ID);

        return clinicAdministrator;
    }

    private Set<Authority> getAuthority(String role) {
        Authority authority = new Authority();
        authority.setName(role);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);

        return authorities;
    }

    private Set<Authority> getClinicAdminAuthority() {
        Authority clinicAdminAuthority = new Authority();
        clinicAdminAuthority.setName(CLINIC_ADMIN_ROLE);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(clinicAdminAuthority);

        return authorities;
    }
}
