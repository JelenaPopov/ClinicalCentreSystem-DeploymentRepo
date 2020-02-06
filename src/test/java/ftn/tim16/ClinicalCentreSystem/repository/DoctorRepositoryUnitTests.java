package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.enumeration.DoctorStatus;
import ftn.tim16.ClinicalCentreSystem.model.Doctor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static ftn.tim16.ClinicalCentreSystem.constants.DoctorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class DoctorRepositoryUnitTests {

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    public void testFindByClinicIdAndSpecializedAndStatusNot_specializedId1() {
        List<Doctor> doctors = doctorRepository.findByClinicIdAndSpecializedIdAndStatusNot(CLINIC_ID, EXAMINATION_TYPE_ID, DOCTOR_STATUS_DELETED);
        assertEquals(NUMBER_OF_DOCTORS_SPECIALIZED_1, doctors.size());
        for (Doctor doctor : doctors) {
            assertEquals(EXAMINATION_TYPE_ID, doctor.getSpecialized().getId());
        }
    }

    @Test
    public void testFindByClinicIdAndSpecializedAndStatusNot_specializedId2() {
        List<Doctor> doctors = doctorRepository.findByClinicIdAndSpecializedIdAndStatusNot(CLINIC_ID, EXAMINATION_TYPE_ID + 1, DOCTOR_STATUS_DELETED);
        assertEquals(NUMBER_OF_DOCTORS_SPECIALIZED_2, doctors.size());
        for (Doctor doctor : doctors) {
            assertEquals(EXAMINATION_TYPE_ID + 1, doctor.getSpecialized().getId());
        }
    }


    @Test
    public void testFindByIdAndStatusIn() {
        List<DoctorStatus> doctorStatuses = new ArrayList<>();
        doctorStatuses.add(NEVER_LOGGED_IN);
        doctorStatuses.add(ACTIVE);

        Doctor doctor = doctorRepository.findByIdAndStatusIn(DOCTOR_ID, doctorStatuses);

        assertEquals(DOCTOR_ID, doctor.getId());
    }


}
