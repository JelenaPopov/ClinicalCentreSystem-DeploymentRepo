package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.model.Clinic;
import ftn.tim16.ClinicalCentreSystem.model.DateTimeInterval;
import ftn.tim16.ClinicalCentreSystem.model.Examination;
import ftn.tim16.ClinicalCentreSystem.model.Room;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static ftn.tim16.ClinicalCentreSystem.constants.ExaminationConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class ExaminationRepositoryUnitTests {

    @Autowired
    private ExaminationRepository examinationRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByRoomIdAndStatusNotAndIntervalEndDateTimeGreaterThanEqualOrderByIntervalStartDateTime() {
        LocalDateTime endDateTime = getLocalDateTime(getDate(DATE), END_TIME_HOUR_2);
        List<Examination> examinations = examinationRepository.findByRoomIdAndStatusNotAndIntervalEndDateTimeGreaterThanEqualOrderByIntervalStartDateTime
                (ROOM_3_ID, EXAMINATION_STATUS_CANCELED, endDateTime);

        assertEquals(NUMBER_OF_APPROVED_EXAMINATION_IN_FUTURE, examinations.size());
        for (Examination examination : examinations) {
            assertEquals(ROOM_3_ID, examination.getRoom().getId());
        }
    }

    @Test
    public void testFindByRoomIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan() {
        LocalDate date = getDate(DATE_2021);
        LocalDateTime greater = LocalDateTime.of(date, LocalTime.of(0, 0));
        LocalDateTime less = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        List<Examination> examinations = examinationRepository.findByRoomIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan
                (ROOM_3_ID, EXAMINATION_STATUS_CANCELED, greater, less);

        assertEquals(NUMBER_OF_APPROVED_EXAMINATION_ON_DATE, examinations.size());
        for (Examination examination : examinations) {
            assertEquals(ROOM_3_ID, examination.getRoom().getId());
        }
    }

    @Test
    public void testFindByRoomIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan_returnEmptyList() {
        LocalDate date = getDate(DATE);
        LocalDateTime greater = LocalDateTime.of(date, LocalTime.of(0, 0));
        LocalDateTime less = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        List<Examination> examinations = examinationRepository.findByRoomIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan
                (ROOM_3_ID, EXAMINATION_STATUS_CANCELED, greater, less);

        assertEquals(NUMBER_OF_APPROVED_EXAMINATION_ON_DATE_EMPTY, examinations.size());
    }

    @Test
    public void testGetByIdAndStatusNot() {
        Examination examination = examinationRepository.getByIdAndStatusNot
                (EXAMINATION_ID, EXAMINATION_STATUS_CANCELED);
        assertEquals(EXAMINATION_ID, examination.getId());
    }

    @Test
    public void testGetByIdAndStatusNot_doesNoExist() {
        Examination examination = examinationRepository.getByIdAndStatusNot
                (EXAMINATION_6_ID, EXAMINATION_STATUS_CANCELED);
        assertNull(examination);
    }

    @Test
    public void testSave() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);
        Room room1 = new Room(ROOM_LABEL_1, EXAMINATION, clinic);
        room1.setId(ROOM_1_ID);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_APPROVED, null, room1, DISCOUNT, null, clinic, null);
        examination.setId(EXAMINATION_ID);

        Examination examinationSaved = this.entityManager.persist(examination);
        entityManager.flush();

        Examination examinationResult = examinationRepository.getByIdAndStatusNot(examinationSaved.getId(), EXAMINATION_STATUS_AWAITING);

        assertNotNull(examinationResult);
        assertEquals(ROOM_LABEL_1, examinationResult.getRoom().getLabel());
    }

    @Test
    public void testFindByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter() {
        List<Examination> examinations = examinationRepository.findByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter
                (CLINIC_ADMIN_ID, AWAITING, EXAMINATION, LocalDateTime.now());

        assertEquals(NUMBER_OF_APPROVED_EXAMINATION_IN_FUTURE, examinations.size());
        for (Examination examination : examinations) {
            assertEquals(CLINIC_ADMIN_ID, examination.getClinicAdministrator().getId());
            assertEquals(AWAITING, examination.getStatus());
        }
    }

    @Test
    public void testFindByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter_clinicAdmin2() {
        List<Examination> examinations = examinationRepository.findByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter
                (CLINIC_ADMIN_2_ID, AWAITING, EXAMINATION, LocalDateTime.now());

        assertEquals(NUMBER_OF_AWAITING_EXAMINATION_IN_FUTURE_CLINIC_ADMIN_2, examinations.size());
        for (Examination examination : examinations) {
            assertEquals(CLINIC_ADMIN_ID, examination.getClinicAdministrator().getId());
            assertEquals(AWAITING, examination.getStatus());
            assertEquals(EXAMINATION, examination.getKind());
        }
    }

    @Test
    public void testFindByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter_clinicAdmin2_returnOperationRoom() {
        List<Examination> examinations = examinationRepository.findByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter
                (CLINIC_ADMIN_2_ID, AWAITING, OPERATION, LocalDateTime.now());

        assertEquals(NUMBER_OF_AWAITING_OPERATION_IN_FUTURE_CLINIC_ADMIN_2, examinations.size());
        for (Examination examination : examinations) {
            assertEquals(CLINIC_ADMIN_2_ID, examination.getClinicAdministrator().getId());
            assertEquals(AWAITING, examination.getStatus());
            assertEquals(OPERATION, examination.getKind());
        }
    }

    private LocalDateTime getLocalDateTime(LocalDate date, String time) throws DateTimeParseException {
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        return LocalDateTime.of(date, localTime);
    }

    private LocalDate getDate(String date) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }


}
