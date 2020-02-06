package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.response.LoggedInUserDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.RequestForTimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.model.DateTimeInterval;
import ftn.tim16.ClinicalCentreSystem.security.auth.JwtAuthenticationRequest;
import ftn.tim16.ClinicalCentreSystem.service.TimeOffNurseService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ftn.tim16.ClinicalCentreSystem.constants.TimeOffNurseConstants.*;
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
public class TimeOffNurseControllerUnitTests {

    private static final String URL_PREFIX = "/api/time-off-nurse";

    @Autowired
    private TestRestTemplate restTemplate;

    private String accessToken;

    private MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private MockMvc mockMvc;

    @MockBean
    private TimeOffNurseService timeOffNurseServiceMocked;

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
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_TO, HOUR, MIN, SEC);
        RequestForTimeOffDTO requestForTimeOffDTO1 = new RequestForTimeOffDTO(ID, "HOLIDAY", new DateTimeInterval(startDate, endDate), NEW_NURSE_FIRST_NAME, NEW_NURSE_lAST_NAME,
                AWAITING);

        List<RequestForTimeOffDTO> requestForTimeOffDTOS = new ArrayList<>();
        requestForTimeOffDTOS.add(requestForTimeOffDTO1);

        Mockito.when(timeOffNurseServiceMocked.getRequestsForHolidayOrTimeOff(ID)).thenReturn(requestForTimeOffDTOS);

        mockMvc.perform(get(URL_PREFIX + "/requests-for-holiday-or-time-off").header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(DB_AWAITING_COUNT)))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(NEW_NURSE_FIRST_NAME)))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(NEW_NURSE_lAST_NAME)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(AWAITING.toString())));

        verify(timeOffNurseServiceMocked, times(1)).getRequestsForHolidayOrTimeOff(ID);
    }

    @Test
    public void testApproveRequestForHolidayOrTimeOff() throws Exception {

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_TO, HOUR, MIN, SEC);

        RequestForTimeOffDTO requestForTimeOffDTOSaved = new RequestForTimeOffDTO(ID, "HOLIDAY", new DateTimeInterval(startDate, endDate), NEW_NURSE_FIRST_NAME, NEW_NURSE_lAST_NAME,
                APPROVED);

        Mockito.when(timeOffNurseServiceMocked.approveRequestForHolidayOrTimeOff(ID)).thenReturn(requestForTimeOffDTOSaved);

        this.mockMvc.perform(put(URL_PREFIX + "/approve-request-for-holiday-or-time-off/" + ID).header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(NEW_NURSE_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(NEW_NURSE_lAST_NAME))
                .andExpect(jsonPath("$.status").value(APPROVED.toString()));
        verify(timeOffNurseServiceMocked, times(1)).approveRequestForHolidayOrTimeOff(ID);
    }

    @Test
    public void testApproveRequestForHolidayOrTimeOff_badRequest() throws Exception {
        Mockito.when(timeOffNurseServiceMocked.approveRequestForHolidayOrTimeOff(APPROVED_TIME_OFF)).thenReturn(null);
        this.mockMvc.perform(put(URL_PREFIX + "/approve-request-for-holiday-or-time-off/" + APPROVED_TIME_OFF).header("Authorization", accessToken))
                .andExpect(status().isBadRequest());
        verify(timeOffNurseServiceMocked, times(1)).approveRequestForHolidayOrTimeOff(APPROVED_TIME_OFF);
    }

    @Test
    public void testRejectRequestForHolidayOrTimeOff() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_TO, HOUR, MIN, SEC);

        RequestForTimeOffDTO requestForTimeOffDTOSaved = new RequestForTimeOffDTO(ID, "HOLIDAY", new DateTimeInterval(startDate, endDate), NEW_NURSE_FIRST_NAME, NEW_NURSE_lAST_NAME,
                REJECTED);

        Mockito.when(timeOffNurseServiceMocked.rejectRequestForHolidayOrTimeOff(ID, REASON_FOR_REJECTION)).thenReturn(requestForTimeOffDTOSaved);

        this.mockMvc.perform(put(URL_PREFIX + "/reject-request-for-holiday-or-time-off/" + ID).contentType(contentType)
                .content(REASON_FOR_REJECTION).header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(NEW_NURSE_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(NEW_NURSE_lAST_NAME))
                .andExpect(jsonPath("$.status").value(REJECTED.toString()));
        verify(timeOffNurseServiceMocked, times(1)).rejectRequestForHolidayOrTimeOff(ID, REASON_FOR_REJECTION);
    }

    @Test
    public void testRejectRequestForHolidayOrTimeOff_badRequest() throws Exception {
        Mockito.when(timeOffNurseServiceMocked.rejectRequestForHolidayOrTimeOff(APPROVED_TIME_OFF, REASON_FOR_REJECTION)).thenReturn(null);
        this.mockMvc.perform(put(URL_PREFIX + "/reject-request-for-holiday-or-time-off/" + APPROVED_TIME_OFF).contentType(contentType)
                .content(REASON_FOR_REJECTION).header("Authorization", accessToken)).andExpect(status().isBadRequest());

        verify(timeOffNurseServiceMocked, times(1)).rejectRequestForHolidayOrTimeOff(APPROVED_TIME_OFF, REASON_FOR_REJECTION);
    }

}
