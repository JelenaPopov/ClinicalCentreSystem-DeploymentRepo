package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.model.TimeOffNurse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static ftn.tim16.ClinicalCentreSystem.constants.TimeOffDoctorConstants.AWAITING;
import static ftn.tim16.ClinicalCentreSystem.constants.TimeOffDoctorConstants.HOLIDAY;
import static ftn.tim16.ClinicalCentreSystem.constants.TimeOffNurseConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class TimeOffNurseRepositoryUnitTests {

    @Autowired
    private TimeOffNurseRepository timeOffNurseRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByNurseClinicIdAndStatus() {
        List<TimeOffNurse> timeOffNursesResults = timeOffNurseRepository.findByNurseClinicIdAndStatus(CLINIC_ID, TIME_OFF_STATUS);
        assertEquals(DB_AWAITING_COUNT, timeOffNursesResults.size());
        for (int i = 0; i < DB_AWAITING_COUNT; i++) {
            assertEquals(TIME_OFF_STATUS, timeOffNursesResults.get(i).getStatus());
        }
    }

    @Test
    public void testFindByIdAndStatus() {
        TimeOffNurse timeOffNursesResult = timeOffNurseRepository.findByIdAndStatus(NURSE_ID_WITH_AWAITING, TIME_OFF_STATUS);
        assertEquals(TIME_OFF_STATUS, timeOffNursesResult.getStatus());
        assertEquals(NURSE_ID_WITH_AWAITING, timeOffNursesResult.getNurse().getId());
    }

    @Test
    public void testFindByIdAndStatus_returnNull() {
        TimeOffNurse timeOffNursesResult = timeOffNurseRepository.findByIdAndStatus(NURSE_ID_WITHOUT_AWAITING, TIME_OFF_STATUS);
        assertNull(timeOffNursesResult);
    }

    @Test
    public void testSave() {
        TimeOffNurse timeOffNurse = new TimeOffNurse(HOLIDAY, null, AWAITING, null);

        TimeOffNurse timeOffNurseSaved = this.entityManager.persist(timeOffNurse);
        entityManager.flush();

        TimeOffNurse timeOffNurseResult = timeOffNurseRepository.findByIdAndStatus(timeOffNurseSaved.getId(), AWAITING);

        assertNotNull(timeOffNurseResult);
        assertEquals(HOLIDAY, timeOffNurseResult.getType());
        assertEquals(TIME_OFF_STATUS, timeOffNurseResult.getStatus());
    }
}
