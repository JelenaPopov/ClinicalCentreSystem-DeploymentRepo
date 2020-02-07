package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.constants.RoomConstants;
import ftn.tim16.ClinicalCentreSystem.dto.response.ExaminationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.ExaminationPagingDTO;
import ftn.tim16.ClinicalCentreSystem.model.*;
import ftn.tim16.ClinicalCentreSystem.repository.ExaminationRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ftn.tim16.ClinicalCentreSystem.constants.ExaminationConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class ExaminationServiceUnitTests {

    @Autowired
    private ExaminationService examinationService;

    @MockBean
    private ExaminationRepository examinationRepositoryMocked;

    @Test
    public void testGetExaminationsAfter() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        room1.setId(ROOM_1_ID);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_APPROVED, null, room1, DISCOUNT, null, clinic, null);
        List<Examination> examinations = new ArrayList<>();
        examinations.add(examination);

        LocalDateTime endDateTime = getLocalDateTime(getDate(DATE), END_TIME_HOUR_2);

        Mockito.when(examinationRepositoryMocked.findByRoomIdAndStatusNotAndIntervalEndDateTimeGreaterThanEqualOrderByIntervalStartDateTime
                (ROOM_1_ID, EXAMINATION_STATUS_CANCELED, endDateTime)).thenReturn(examinations);

        List<Examination> examinationsResults = examinationService.getExaminationsAfter(
                ROOM_1_ID, endDateTime);

        Assert.assertNotNull(examinationsResults);
        Assert.assertEquals(examinations.size(), examinationsResults.size());

        verify(examinationRepositoryMocked, times(1)).findByRoomIdAndStatusNotAndIntervalEndDateTimeGreaterThanEqualOrderByIntervalStartDateTime
                (ROOM_1_ID, EXAMINATION_STATUS_CANCELED, endDateTime);
    }

    @Test
    public void testGetExaminationsOnDay() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        room1.setId(ROOM_1_ID);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_APPROVED, null, room1, DISCOUNT, null, clinic, null);
        List<Examination> examinations = new ArrayList<>();
        examinations.add(examination);

        LocalDateTime startDateTime = getLocalDateTime(getDate(DATE), SEARCH_START_TIME);

        LocalDate date = getDate(DATE);
        LocalDateTime greater = LocalDateTime.of(date, LocalTime.of(0, 0));

        LocalDateTime less = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        Mockito.when(examinationRepositoryMocked.findByRoomIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan
                (ROOM_1_ID, EXAMINATION_STATUS_CANCELED, greater, less)).thenReturn(examinations);

        List<Examination> examinationsResults = examinationService.getExaminationsOnDay(
                ROOM_1_ID, startDateTime);

        Assert.assertNotNull(examinationsResults);
        Assert.assertEquals(examinations.size(), examinationsResults.size());

        verify(examinationRepositoryMocked, times(1)).findByRoomIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan
                (ROOM_1_ID, EXAMINATION_STATUS_CANCELED, greater, less);
    }


    @Test
    public void testGetExamination() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_APPROVED, null, null, DISCOUNT, null, clinic, null);
        examination.setId(EXAMINATION_ID);

        Mockito.when(examinationRepositoryMocked.getByIdAndStatusNot
                (EXAMINATION_ID, EXAMINATION_STATUS_CANCELED)).thenReturn(examination);

        Examination examinationResult = examinationService.getExamination(EXAMINATION_ID);

        Assert.assertNotNull(examinationResult);
        Assert.assertEquals(EXAMINATION_ID, examinationResult.getId());

        verify(examinationRepositoryMocked, times(1)).getByIdAndStatusNot
                (EXAMINATION_ID, EXAMINATION_STATUS_CANCELED);
    }

    @Test
    @Transactional
    public void testAssignRoom() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);
        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        room1.setId(ROOM_1_ID);
        Nurse nurse = new Nurse(NEW_NURSE_EMAIL, NEW_NURSE_PASSWORD, NEW_NURSE_FIRST_NAME, NEW_NURSE_lAST_NAME, NEW_NURSE_PHONE_NUMBER,
                LocalTime.parse(NEW_NURSE_WORK_HOURS_FROM), LocalTime.parse(NEW_NURSE_WORK_HOURS_TO), clinic, getAuthority(NEW_NURSE_AUTHORITY));

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, null);
        examination.setId(EXAMINATION_ID);

        Examination savedExamination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_APPROVED, null, room1, DISCOUNT, nurse, clinic, null);
        savedExamination.setId(EXAMINATION_ID);

        Mockito.when(examinationRepositoryMocked.save(savedExamination)).thenReturn(savedExamination);

        Examination examinationResult = examinationService.assignRoom(examination, room1, nurse);

        Assert.assertNotNull(examinationResult);
        Assert.assertEquals(savedExamination.getStatus(), examinationResult.getStatus());

        verify(examinationRepositoryMocked, times(1)).save(savedExamination);
    }


    @Test
    public void testGetAwaitingExaminations() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        ClinicAdministrator clinicAdministrator = getLoggedInClinicAdmin();
        Examination examination1 = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, clinicAdministrator);
        Examination examination2 = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, clinicAdministrator);

        List<Examination> examinations = new ArrayList<>();
        examinations.add(examination1);
        examinations.add(examination2);
        Page<Examination> examinationPage = new PageImpl(examinations);

        Mockito.when(examinationRepositoryMocked.findByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter
                (eq(CLINIC_ADMIN_ID), eq(AWAITING), eq(EXAMINATION), any(LocalDateTime.class))).thenReturn(examinations);
        Mockito.when(examinationRepositoryMocked.findByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter
                (eq(CLINIC_ADMIN_ID), eq(AWAITING), eq(EXAMINATION), any(LocalDateTime.class), eq(PAGE))).thenReturn(examinationPage);

        ExaminationPagingDTO examinationsResults = examinationService.getAwaitingExaminations(
                EXAMINATION.toString(), getLoggedInClinicAdmin(), PAGE);

        Assert.assertNotNull(examinationsResults);
        Assert.assertEquals(examinations.size(), examinationsResults.getExaminationList().size());
        for (ExaminationDTO examination : examinationsResults.getExaminationList()) {
            Assert.assertEquals(AWAITING, examination.getStatus());
        }

        verify(examinationRepositoryMocked, times(1)).findByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter
                (eq(CLINIC_ADMIN_ID), eq(AWAITING), eq(EXAMINATION), any(LocalDateTime.class));
        verify(examinationRepositoryMocked, times(1)).findByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter
                (eq(CLINIC_ADMIN_ID), eq(AWAITING), eq(EXAMINATION), any(LocalDateTime.class), eq(PAGE));
    }

    private LocalDateTime getLocalDateTime(LocalDate date, String time) throws DateTimeParseException {
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        return LocalDateTime.of(date, localTime);
    }

    private LocalDate getDate(String date) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    private Set<Authority> getAuthority(String role) {
        Authority authority = new Authority();
        authority.setName(role);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);

        return authorities;
    }

    private ClinicAdministrator getLoggedInClinicAdmin() {
        Clinic clinic = new Clinic(RoomConstants.NEW_CLINIC_NAME, RoomConstants.NEW_CLINIC_ADDRESS, RoomConstants.NEW_CLINIC_DESCRIPTION);
        clinic.setId(RoomConstants.CLINIC_ID);
        ClinicAdministrator clinicAdministrator = new ClinicAdministrator(CLINIC_ADMIN_EMAIL, CLINIC_ADMIN_PASSWORD,
                CLINIC_ADMIN_FIRST_NAME, CLINIC_ADMIN_LAST_NAME, CLINIC_ADMIN_PHONE_NUMBER, clinic, getClinicAdminAuthority());
        clinicAdministrator.setId(CLINIC_ADMIN_ID);

        return clinicAdministrator;
    }

    private Set<Authority> getClinicAdminAuthority() {
        Authority clinicAdminAuthority = new Authority();
        clinicAdminAuthority.setName(CLINIC_ADMIN_ROLE);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(clinicAdminAuthority);

        return authorities;
    }
}
