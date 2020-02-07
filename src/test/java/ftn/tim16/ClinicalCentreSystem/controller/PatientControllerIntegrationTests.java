package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.TestUtil;
import ftn.tim16.ClinicalCentreSystem.dto.response.LoggedInUserDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.PatientStatus;
import ftn.tim16.ClinicalCentreSystem.security.auth.JwtAuthenticationRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import static ftn.tim16.ClinicalCentreSystem.constants.PatientConstants.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class PatientControllerIntegrationTests {

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
    }

    @Test
    @Sql(scripts = "classpath:update-data-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testApproveRequestToRegister_Success() throws Exception {
        this.mockMvc.perform(put(URL_PREFIX + "/approve-request-to-register/" + PATIENT_ID)
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(PATIENT_ID.intValue()))
                .andExpect(jsonPath("$.phoneNumber").value(PATIENT_PHONE_NUMBER))
                .andExpect(jsonPath("$.healthInsuranceID").value(PATIENT_HEALTH_INSURANCE_ID))
                .andExpect(jsonPath("$.status").value(PatientStatus.APPROVED.toString()));
    }

    @Test
    public void testApproveRequestToRegister_BadRequest() throws Exception {
        this.mockMvc.perform(put(URL_PREFIX + "/approve-request-to-register/" + ACTIVATED_PATIENT_ID)
                .header("Authorization", accessToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "classpath:insert-data-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testRejectRequestToRegister_Success() throws Exception {
        String jsonBody = TestUtil.json(REASON_FOR_REJECTION);

        this.mockMvc.perform(put(URL_PREFIX + "/reject-request-to-register/" + PATIENT_ID)
                .header("Authorization", accessToken)
                .contentType(contentType)
                .content(jsonBody))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRejectRequestToRegister_NotFound() throws Exception {
        String jsonBody = TestUtil.json(REASON_FOR_REJECTION);

        this.mockMvc.perform(put(URL_PREFIX + "/reject-request-to-register/" + ACTIVATED_PATIENT_ID)
                .header("Authorization", accessToken)
                .contentType(contentType)
                .content(jsonBody))
                .andExpect(status().isNotFound());
    }
}
