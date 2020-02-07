package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.request.AwaitingApprovalPatientDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.PatientWithIdDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.LoggedInUserDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.PatientStatus;
import ftn.tim16.ClinicalCentreSystem.security.auth.JwtAuthenticationRequest;
import ftn.tim16.ClinicalCentreSystem.service.PatientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static ftn.tim16.ClinicalCentreSystem.constants.PatientConstants.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class PatientControllerUnitTests {

    public static final String URL_PREFIX = "/api/patient";

    private String accessToken;

    private MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private PatientService patientServiceMock;

    @PostConstruct
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Before
    public void login() {
        ResponseEntity<LoggedInUserDTO> responseEntity = restTemplate.postForEntity("/api/auth/login",
                new JwtAuthenticationRequest("1st.Admin@maildrop.cc", "1st.Admin"), LoggedInUserDTO.class);
        accessToken = "Bearer " + responseEntity.getBody().getUserTokenState().getAccessToken();
    }

    @Test
    public void testGetAllRequestsToRegister_Success() throws Exception {
        AwaitingApprovalPatientDTO patient1 = new AwaitingApprovalPatientDTO(PATIENT_ID, PATIENT_FIRST_NAME, PATIENT_LAST_NAME,
                PATIENT_EMAIL, PATIENT_STATUS_AWAITING_APPROVAL);

        AwaitingApprovalPatientDTO patient2 = new AwaitingApprovalPatientDTO(NEW_PATIENT_ID, NEW_PATIENT_FIRST_NAME, NEW_PATIENT_LAST_NAME,
                NEW_PATIENT_EMAIL, NEW_PATIENT_STATUS_AWAITING_APPROVAL);

        List<AwaitingApprovalPatientDTO> patients = new ArrayList<>();
        patients.add(patient1);
        patients.add(patient2);

        Mockito.when(patientServiceMock.findByStatus(PATIENT_STATUS_AWAITING_APPROVAL)).thenReturn(patients);

        mockMvc.perform(get(URL_PREFIX + "/all-requests-to-register")
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(DB_AWAITING_APPROVAL_COUNT)))
                .andExpect(jsonPath("$.[*].id").value(hasItem(PATIENT_ID.intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(PATIENT_FIRST_NAME)))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(PATIENT_LAST_NAME)))
                .andExpect(jsonPath("$.[*].email").value(hasItem(PATIENT_EMAIL)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(PATIENT_STATUS_AWAITING_APPROVAL.toString())));

        verify(patientServiceMock, times(1)).findByStatus(PATIENT_STATUS_AWAITING_APPROVAL);
    }

    @Test
    public void testApproveRequestToRegister_Success() throws Exception {
        PatientWithIdDTO approvedPatient = new PatientWithIdDTO(PATIENT_ID, PATIENT_EMAIL, PATIENT_FIRST_NAME, PATIENT_LAST_NAME,
                PATIENT_PHONE_NUMBER, PATIENT_ADDRESS, PATIENT_CITY, PATIENT_COUNTRY, PATIENT_HEALTH_INSURANCE_ID,
                PatientStatus.APPROVED);

        Mockito.when(patientServiceMock.approveRequestToRegister(PATIENT_ID)).thenReturn(approvedPatient);

        this.mockMvc.perform(put(URL_PREFIX + "/approve-request-to-register/" + PATIENT_ID)
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(PATIENT_ID.intValue()))
                .andExpect(jsonPath("$.phoneNumber").value(PATIENT_PHONE_NUMBER))
                .andExpect(jsonPath("$.healthInsuranceID").value(PATIENT_HEALTH_INSURANCE_ID))
                .andExpect(jsonPath("$.status").value(PatientStatus.APPROVED.toString()));

        verify(patientServiceMock, times(1)).approveRequestToRegister(PATIENT_ID);
    }

    @Test
    public void testApproveRequestToRegister_BadRequest() throws Exception {
        Mockito.when(patientServiceMock.approveRequestToRegister(ACTIVATED_PATIENT_ID)).thenReturn(null);

        this.mockMvc.perform(put(URL_PREFIX + "/approve-request-to-register/" + ACTIVATED_PATIENT_ID)
                .header("Authorization", accessToken))
                .andExpect(status().isBadRequest());

        verify(patientServiceMock, times(1)).approveRequestToRegister(ACTIVATED_PATIENT_ID);
    }

    @Test
    public void testRejectRequestToRegister_Success() throws Exception {
        Mockito.when(patientServiceMock.rejectRequestToRegister(PATIENT_ID, REASON_FOR_REJECTION)).thenReturn(true);

        this.mockMvc.perform(put(URL_PREFIX + "/reject-request-to-register/" + PATIENT_ID)
                .header("Authorization", accessToken)
                .contentType(contentType)
                .content(REASON_FOR_REJECTION))
                .andExpect(status().isNoContent());

        verify(patientServiceMock, times(1)).rejectRequestToRegister(PATIENT_ID, REASON_FOR_REJECTION);
    }

    @Test
    public void testRejectRequestToRegister_NotFound() throws Exception {
        Mockito.when(patientServiceMock.rejectRequestToRegister(ACTIVATED_PATIENT_ID, REASON_FOR_REJECTION)).thenReturn(false);

        this.mockMvc.perform(put(URL_PREFIX + "/reject-request-to-register/" + ACTIVATED_PATIENT_ID)
                .header("Authorization", accessToken)
                .contentType(contentType)
                .content(REASON_FOR_REJECTION))
                .andExpect(status().isNotFound());

        verify(patientServiceMock, times(1)).rejectRequestToRegister(ACTIVATED_PATIENT_ID, REASON_FOR_REJECTION);
    }
}
