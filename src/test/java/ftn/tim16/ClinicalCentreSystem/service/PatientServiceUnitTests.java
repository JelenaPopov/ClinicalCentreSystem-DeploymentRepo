package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.request.AwaitingApprovalPatientDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.PatientWithIdDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.PatientStatus;
import ftn.tim16.ClinicalCentreSystem.model.Authority;
import ftn.tim16.ClinicalCentreSystem.model.MedicalRecord;
import ftn.tim16.ClinicalCentreSystem.model.Patient;
import ftn.tim16.ClinicalCentreSystem.repository.PatientRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ftn.tim16.ClinicalCentreSystem.constants.PatientConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class PatientServiceUnitTests {

    @Autowired
    private PatientService patientService;

    @MockBean
    private PatientRepository patientRepositoryMock;

    @MockBean
    private EmailNotificationService emailNotificationServiceMock;

    @MockBean
    private MedicalRecordService medicalRecordServiceMock;

    @Test
    public void testFindByStatus() {
        Patient patient1 = new Patient(PATIENT_EMAIL, PATIENT_PASSWORD, PATIENT_FIRST_NAME, PATIENT_LAST_NAME,
                PATIENT_PHONE_NUMBER, PATIENT_ADDRESS, PATIENT_CITY, PATIENT_COUNTRY, PATIENT_HEALTH_INSURANCE_ID,
                getPatientAuthority());
        patient1.setId(PATIENT_ID);

        Patient patient2 = new Patient(NEW_PATIENT_EMAIL, NEW_PATIENT_PASSWORD, NEW_PATIENT_FIRST_NAME, NEW_PATIENT_LAST_NAME,
                NEW_PATIENT_PHONE_NUMBER, NEW_PATIENT_ADDRESS, NEW_PATIENT_CITY, NEW_PATIENT_COUNTRY, NEW_PATIENT_HEALTH_INSURANCE_ID,
                getPatientAuthority());
        patient2.setId(NEW_PATIENT_ID);

        List<Patient> patients = new ArrayList<>();
        patients.add(patient1);
        patients.add(patient2);

        Mockito.when(patientRepositoryMock.findByStatus(PATIENT_STATUS_AWAITING_APPROVAL)).thenReturn(patients);

        List<AwaitingApprovalPatientDTO> patientsResult = patientService.findByStatus(PATIENT_STATUS_AWAITING_APPROVAL);

        assertEquals(patients.size(), patientsResult.size());
        for (int i = 0; i < patients.size(); i++) {
            assertEquals(patients.get(i).getId(), patientsResult.get(i).getId());
            assertEquals(patients.get(i).getStatus(), patientsResult.get(i).getStatus());
        }
    }

    @Test
    public void testApproveRequestToRegister_Success() {
        Patient patient = new Patient(PATIENT_EMAIL, PATIENT_PASSWORD, PATIENT_FIRST_NAME, PATIENT_LAST_NAME,
                PATIENT_PHONE_NUMBER, PATIENT_ADDRESS, PATIENT_CITY, PATIENT_COUNTRY, PATIENT_HEALTH_INSURANCE_ID,
                getPatientAuthority());
        patient.setId(PATIENT_ID);

        Patient approvedPatient = new Patient(PATIENT_EMAIL, PATIENT_PASSWORD, PATIENT_FIRST_NAME, PATIENT_LAST_NAME,
                PATIENT_PHONE_NUMBER, PATIENT_ADDRESS, PATIENT_CITY, PATIENT_COUNTRY, PATIENT_HEALTH_INSURANCE_ID,
                getPatientAuthority());
        approvedPatient.setId(PATIENT_ID);
        approvedPatient.setStatus(PatientStatus.APPROVED);

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatient(approvedPatient);

        Mockito.when(patientRepositoryMock.findByIdAndStatus(PATIENT_ID, PATIENT_STATUS_AWAITING_APPROVAL)).thenReturn(patient);
        Mockito.when(patientRepositoryMock.saveAndFlush(approvedPatient)).thenReturn(approvedPatient);
        Mockito.when(medicalRecordServiceMock.create(approvedPatient)).thenReturn(medicalRecord);
        Mockito.doNothing().when(emailNotificationServiceMock).sendEmail(anyString(), anyString(), anyString());

        PatientWithIdDTO patientDTOResult = patientService.approveRequestToRegister(patient.getId());

        assertEquals(PATIENT_ID, patientDTOResult.getId());
        assertEquals(PatientStatus.APPROVED, patientDTOResult.getStatus());

        verify(patientRepositoryMock, times(1)).findByIdAndStatus(PATIENT_ID, PATIENT_STATUS_AWAITING_APPROVAL);
        verify(patientRepositoryMock, times(1)).saveAndFlush(approvedPatient);
        verify(medicalRecordServiceMock, times(1)).create(approvedPatient);
        verify(emailNotificationServiceMock, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void testApproveRequestToRegister_PatientNotFound() {
        Mockito.when(patientRepositoryMock.findByIdAndStatus(PATIENT_ID, PATIENT_STATUS_AWAITING_APPROVAL)).thenReturn(null);

        PatientWithIdDTO patientDTOResult = patientService.approveRequestToRegister(PATIENT_ID);

        assertNull(patientDTOResult);

        verify(patientRepositoryMock, times(1)).findByIdAndStatus(PATIENT_ID, PATIENT_STATUS_AWAITING_APPROVAL);
    }

    @Test
    public void testRejectRequestToRegister_Success() {
        Patient patient = new Patient(PATIENT_EMAIL, PATIENT_PASSWORD, PATIENT_FIRST_NAME, PATIENT_LAST_NAME,
                PATIENT_PHONE_NUMBER, PATIENT_ADDRESS, PATIENT_CITY, PATIENT_COUNTRY, PATIENT_HEALTH_INSURANCE_ID,
                getPatientAuthority());
        patient.setId(PATIENT_ID);

        Mockito.when(patientRepositoryMock.findByIdAndStatus(PATIENT_ID, PATIENT_STATUS_AWAITING_APPROVAL)).thenReturn(patient);
        Mockito.doNothing().when(patientRepositoryMock).deleteById(PATIENT_ID);
        Mockito.doNothing().when(emailNotificationServiceMock).sendEmail(anyString(), anyString(), anyString());

        boolean isDeletedResult = patientService.rejectRequestToRegister(patient.getId(), REASON_FOR_REJECTION);

        assertTrue(isDeletedResult);

        verify(patientRepositoryMock, times(1)).findByIdAndStatus(PATIENT_ID, PATIENT_STATUS_AWAITING_APPROVAL);
        verify(patientRepositoryMock, times(1)).deleteById(patient.getId());
    }

    @Test
    public void testRejectRequestToRegister_PatientNotFound() {
        Mockito.when(patientRepositoryMock.findByIdAndStatus(PATIENT_ID, PATIENT_STATUS_AWAITING_APPROVAL)).thenReturn(null);

        boolean isDeletedResult = patientService.rejectRequestToRegister(PATIENT_ID, REASON_FOR_REJECTION);

        assertFalse(isDeletedResult);

        verify(patientRepositoryMock, times(1)).findByIdAndStatus(PATIENT_ID, PATIENT_STATUS_AWAITING_APPROVAL);
    }

    private Set<Authority> getPatientAuthority() {
        Authority patientAuthority = new Authority();
        patientAuthority.setName(PATIENT_ROLE);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(patientAuthority);

        return authorities;
    }
}
