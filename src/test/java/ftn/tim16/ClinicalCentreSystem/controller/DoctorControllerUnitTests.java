package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.DoctorDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.LoggedInUserDTO;
import ftn.tim16.ClinicalCentreSystem.model.Clinic;
import ftn.tim16.ClinicalCentreSystem.model.ExaminationType;
import ftn.tim16.ClinicalCentreSystem.security.auth.JwtAuthenticationRequest;
import ftn.tim16.ClinicalCentreSystem.service.DoctorService;
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

import static ftn.tim16.ClinicalCentreSystem.constants.DoctorConstants.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DoctorControllerUnitTests {

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

    @MockBean
    private DoctorService doctorServiceMock;

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
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_DESCRIPTION, NEW_CLINIC_ADDRESS);
        clinic.setId(CLINIC_ID);
        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);
        examinationType.setId(EXAMINATION_TYPE_ID);
        DoctorDTO doctor1 = new DoctorDTO(NEW_DOCTOR_ID, NEW_DOCTOR_PHONE_NUMBER, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_LAST_NAME,
                NEW_DOCTOR_WORK_HOURS_FROM, NEW_DOCTOR_WORK_HOURS_TO, NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, examinationType, NEW_DOCTOR_RATING);
        DoctorDTO doctor2 = new DoctorDTO(NEW_DOCTOR_2_ID, NEW_DOCTOR_2_PHONE_NUMBER, NEW_DOCTOR_2_FIRST_NAME, NEW_DOCTOR_2_LAST_NAME,
                NEW_DOCTOR_2_WORK_HOURS_FROM, NEW_DOCTOR_2_WORK_HOURS_TO, NEW_DOCTOR_2_EMAIL, NEW_DOCTOR_2_PASSWORD, examinationType, NEW_DOCTOR_2_RATING);
        List<DoctorDTO> doctorDTOList = new ArrayList<>();
        doctorDTOList.add(doctor1);
        doctorDTOList.add(doctor2);

        Mockito.when(doctorServiceMock.getAllAvailableDoctors(EXAMINATION_TYPE_ID, CLINIC_ID, START_DATE_TIME, END_DATE_TIME)).thenReturn(doctorDTOList);

        mockMvc.perform(get(URL_PREFIX + "/available")
                .param("specialized", EXAMINATION_TYPE_ID.toString())
                .param("startDateTime", START_DATE_TIME)
                .param("endDateTime", END_DATE_TIME)
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$").value(hasSize(LIST_AVAILABLE_DOCTORS_COUNT)))
                .andExpect(jsonPath("$.[*].id").value(hasItem(NEW_DOCTOR_ID.intValue())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(NEW_DOCTOR_2_ID.intValue())));

        verify(doctorServiceMock, times(1)).getAllAvailableDoctors(EXAMINATION_TYPE_ID, CLINIC_ID, START_DATE_TIME, END_DATE_TIME);
    }

}
