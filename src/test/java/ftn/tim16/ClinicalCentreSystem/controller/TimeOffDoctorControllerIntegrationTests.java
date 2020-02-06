package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.response.LoggedInUserDTO;
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

import static ftn.tim16.ClinicalCentreSystem.constants.TimeOffDoctorConstants.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class TimeOffDoctorControllerIntegrationTests {

    private static final String URL_PREFIX = "/api/time-off-doctor";

    @Autowired
    private TestRestTemplate restTemplate;

    private String accessToken;

    private MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    public void setUp() {
        this.mockMvc = MockMvcBuilders.
                webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Before
    public void login() {
        ResponseEntity<LoggedInUserDTO> responseEntity =
                restTemplate.postForEntity("/api/auth/login",
                        new JwtAuthenticationRequest("ClinicAdmin1@maildrop.cc", "ClinicAdmin1"), LoggedInUserDTO.class);

        accessToken = "Bearer " + responseEntity.getBody().getUserTokenState().getAccessToken();
    }

    @Test
    public void testGetRequestsForHolidayOrTimeOff() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/requests-for-holiday-or-time-off").header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(DB_AWAITING_COUNT)))
                .andExpect(jsonPath("$.[*].type").value(hasItem(HOLIDAY.toString().toLowerCase())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DB_DOCTOR_FIRST_NAME)))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DB_DOCTOR_LAST_NAME)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(AWAITING.toString())));
    }

    @Test
    @Sql(
            scripts = "classpath:update-data-h2.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void testApproveRequestForHolidayOrTimeOff() throws Exception {

        this.mockMvc.perform(put(URL_PREFIX + "/approve-request-for-holiday-or-time-off/" + DB_AWAITING_TIME_OFF).header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(DB_DOCTOR_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(DB_DOCTOR_LAST_NAME))
                .andExpect(jsonPath("$.status").value(APPROVED.toString()));
    }

    @Test
    public void testApproveRequestForHolidayOrTimeOff_badRequest() throws Exception {

        this.mockMvc.perform(put(URL_PREFIX + "/approve-request-for-holiday-or-time-off/" + DB_APPROVED_TIME_OFF).header("Authorization", accessToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(
            scripts = "classpath:update-data-h2.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void testRejectRequestForHolidayOrTimeOff() throws Exception {
        this.mockMvc.perform(put(URL_PREFIX + "/reject-request-for-holiday-or-time-off/" + DB_AWAITING_TIME_OFF).contentType(contentType)
                .content(REASON_FOR_REJECTION).header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(DB_DOCTOR_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(DB_DOCTOR_LAST_NAME))
                .andExpect(jsonPath("$.status").value(REJECTED.toString()));
    }

    @Test
    public void testRejectRequestForHolidayOrTimeOff_badRequest() throws Exception {
        this.mockMvc.perform(put(URL_PREFIX + "/reject-request-for-holiday-or-time-off/" + DB_APPROVED_TIME_OFF).contentType(contentType)
                .content(REASON_FOR_REJECTION).header("Authorization", accessToken)).andExpect(status().isBadRequest());
    }
}

