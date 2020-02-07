package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.model.TimeOffDoctor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static ftn.tim16.ClinicalCentreSystem.constants.TimeOffDoctorConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class TimeOffDoctorRepositoryUnitTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeOffDoctorRepository timeOffDoctorRepository;


    @Test
    public void testFindByDoctorClinicIdAndStatus() {
        List<TimeOffDoctor> timeOffDoctorsResults = timeOffDoctorRepository.findByDoctorClinicIdAndStatus(CLINIC_ID, TIME_OFF_STATUS_AWAITING);
        assertEquals(DB_AWAITING_COUNT, timeOffDoctorsResults.size());
        for (TimeOffDoctor timeOffDoctor : timeOffDoctorsResults) {
            assertEquals(TIME_OFF_STATUS_AWAITING, timeOffDoctor.getStatus());
        }
    }

    @Test
    public void testFindByIdAndStatus() {
        TimeOffDoctor timeOffDoctorResult = timeOffDoctorRepository.findByIdAndStatus(DOCTOR_ID_WITH_AWAITING, TIME_OFF_STATUS_AWAITING);
        assertEquals(TIME_OFF_STATUS_AWAITING, timeOffDoctorResult.getStatus());
        assertEquals(DOCTOR_ID_WITH_AWAITING, timeOffDoctorResult.getDoctor().getId());
    }

    @Test
    public void testFindByIdAndStatus_returnNull() {
        TimeOffDoctor timeOffDoctorResult = timeOffDoctorRepository.findByIdAndStatus(DOCTOR_ID_WITHOUT_AWAITING, TIME_OFF_STATUS_AWAITING);
        assertNull(timeOffDoctorResult);
    }

    @Test
    public void testSave() {
        TimeOffDoctor timeOffDoctor = new TimeOffDoctor(HOLIDAY, null, AWAITING, null);

        TimeOffDoctor timeOffDoctorSaved = this.entityManager.persist(timeOffDoctor);
        entityManager.flush();

        TimeOffDoctor timeOffDoctorResult = timeOffDoctorRepository.findByIdAndStatus(timeOffDoctorSaved.getId(), AWAITING);

        assertNotNull(timeOffDoctorResult);
        assertEquals(HOLIDAY, timeOffDoctorResult.getType());
        assertEquals(TIME_OFF_STATUS_AWAITING, timeOffDoctorResult.getStatus());
    }

}
