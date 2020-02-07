package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.response.ExaminationPagingDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.LoggedInUserDTO;
import ftn.tim16.ClinicalCentreSystem.model.Clinic;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;
import ftn.tim16.ClinicalCentreSystem.model.DateTimeInterval;
import ftn.tim16.ClinicalCentreSystem.model.Examination;
import ftn.tim16.ClinicalCentreSystem.security.auth.JwtAuthenticationRequest;
import ftn.tim16.ClinicalCentreSystem.service.ExaminationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ftn.tim16.ClinicalCentreSystem.constants.ExaminationConstants.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class ExaminationControllerUnitTests {

    public static final String URL_PREFIX = "/api/examination";

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
    private ExaminationService examinationServiceMock;

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
    public void testGetAwaitingExaminations_Success() throws Exception {
        Pageable page = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Examination examination1 = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, null);
        Examination examination2 = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, null);

        List<Examination> examinations = new ArrayList<>();
        examinations.add(examination1);
        examinations.add(examination2);
        ExaminationPagingDTO examinationPagingDTO = new ExaminationPagingDTO(examinations, LIST_EXAMINATIONS_COUNT);

        Mockito.when(examinationServiceMock
                .getAwaitingExaminations(eq(KIND_EXAMINATION), any(ClinicAdministrator.class), eq(page))).thenReturn(examinationPagingDTO);

        mockMvc.perform(get(URL_PREFIX + "/get-awaiting")
                .param("kind", KIND_EXAMINATION)
                .param("page", PAGE_NUMBER.toString())
                .param("size", PAGE_SIZE.toString())
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.examinationList").value(hasSize(LIST_EXAMINATIONS_COUNT)))
                .andExpect(jsonPath("$.examinationList.[*].status").value(hasItem(EXAMINATION_STATUS_AWAITING.toString())))
                .andExpect(jsonPath("$.numberOfItems").value(LIST_EXAMINATIONS_COUNT));

        verify(examinationServiceMock, times(1))
                .getAwaitingExaminations(eq(KIND_EXAMINATION), any(ClinicAdministrator.class), eq(page));
    }

}
