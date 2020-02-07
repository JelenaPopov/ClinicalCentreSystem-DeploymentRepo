package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.model.Authority;
import ftn.tim16.ClinicalCentreSystem.model.Patient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ftn.tim16.ClinicalCentreSystem.constants.PatientConstants.*;
import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class PatientRepositoryUnitTests {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByStatus_Success() {
        List<Patient> patientsResult = patientRepository.findByStatus(PATIENT_STATUS_AWAITING_APPROVAL);

        assertEquals(DB_AWAITING_APPROVAL_COUNT, patientsResult.size());
        for (Patient patient : patientsResult) {
            assertEquals(PATIENT_STATUS_AWAITING_APPROVAL, patient.getStatus());
        }
    }

    @Test
    public void testFindByIdAndStatus_Success() {
        Patient patientResult = patientRepository.findByIdAndStatus(PATIENT_ID, PATIENT_STATUS_AWAITING_APPROVAL);

        assertNotNull(patientResult);
        assertEquals(PATIENT_ID, patientResult.getId());
        assertEquals(PATIENT_STATUS_AWAITING_APPROVAL, patientResult.getStatus());
        assertEquals(PATIENT_EMAIL, patientResult.getEmail());
        assertEquals(PATIENT_HEALTH_INSURANCE_ID, patientResult.getHealthInsuranceId());
    }

    @Test
    public void testFindByIdAndStatus_ReturnNull() {
        Patient patientResult = patientRepository.findByIdAndStatus(ACTIVATED_PATIENT_ID, PATIENT_STATUS_AWAITING_APPROVAL);

        assertNull(patientResult);
    }

    @Test
    public void testSave_Success() {
        Patient patient = new Patient(NEW_PATIENT_EMAIL, NEW_PATIENT_PASSWORD, NEW_PATIENT_FIRST_NAME, NEW_PATIENT_LAST_NAME,
                NEW_PATIENT_PHONE_NUMBER, NEW_PATIENT_ADDRESS, NEW_PATIENT_CITY, NEW_PATIENT_COUNTRY, NEW_PATIENT_HEALTH_INSURANCE_ID,
                getPatientAuthority());

        entityManager.persist(patient);

        Patient patientResult = patientRepository.findByEmail(NEW_PATIENT_EMAIL);

        assertNotNull(patientResult);
        assertEquals(NEW_PATIENT_STATUS_AWAITING_APPROVAL, patientResult.getStatus());
        assertEquals(NEW_PATIENT_EMAIL, patientResult.getEmail());
        assertEquals(NEW_PATIENT_HEALTH_INSURANCE_ID, patientResult.getHealthInsuranceId());
    }

    @Test
    public void testDelete_Success() {
        Patient patient = patientRepository.findByEmail(PATIENT_EMAIL);
        assertNotNull(patient);

        entityManager.remove(patient);

        Patient patientResult = patientRepository.findByEmail(PATIENT_EMAIL);
        assertNull(patientResult);
    }

    private Set<Authority> getPatientAuthority() {
        Authority patientAuthority = new Authority();
        patientAuthority.setName(PATIENT_ROLE);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(patientAuthority);

        return authorities;
    }
}
