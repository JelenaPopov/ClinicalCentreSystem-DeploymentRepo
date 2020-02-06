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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import static ftn.tim16.ClinicalCentreSystem.constants.DoctorConstants.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DoctorControllerIntegrationTests {

    public static final String URL_PREFIX = "/api/doctor";

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
                new JwtAuthenticationRequest("ClinicAdmin1@maildrop.cc", "ClinicAdmin1"), LoggedInUserDTO.class);
        accessToken = "Bearer " + responseEntity.getBody().getUserTokenState().getAccessToken();
    }

    @Test
    public void testGetAllAvailableDoctors_Success() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/available")
                .param("specialized", EXAMINATION_TYPE_ID.toString())
                .param("startDateTime", START_DATE_TIME)
                .param("endDateTime", END_DATE_TIME)
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$").value(hasSize(DB_AVAILABLE_DOCTORS_COUNT)))
                .andExpect(jsonPath("$.[*].id").value(hasItem(NEW_DOCTOR_ID.intValue())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(NEW_DOCTOR_2_ID.intValue())));
    }
}
