package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.TestUtil;
import ftn.tim16.ClinicalCentreSystem.dto.request.AssignExaminationDTO;
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

import static ftn.tim16.ClinicalCentreSystem.constants.RoomConstants.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class RoomControllerIntegrationTests {

    public static final String URL_PREFIX = "/api/room";

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
    public void testSearchRoomsInClinic_Success() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/pageAll")
                .param("page", PAGE_NUMBER.toString())
                .param("size", PAGE_SIZE.toString())
                .param("kind", KIND_EXAMINATION)
                .param("searchLabel", SEARCH_LABEL)
                .param("searchDate", SEARCH_DATE)
                .param("searchStartTime", SEARCH_START_TIME)
                .param("searchEndTime", SEARCH_END_TIME)
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.numberOfItems").value(LIST_ROOMS_COUNT))
                .andExpect(jsonPath("$.roomDTOList").value(hasSize(LIST_ROOMS_COUNT)))
                .andExpect(jsonPath("$.roomDTOList.[*].id").value(hasItem(ROOM_1_ID.intValue())))
                .andExpect(jsonPath("$.roomDTOList.[*].id").value(hasItem(ROOM_2_ID.intValue())));
    }

    @Test
    public void testSearchRoomsInClinic_BadRequest() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/pageAll")
                .param("page", PAGE_NUMBER.toString())
                .param("size", PAGE_SIZE.toString())
                .param("kind", KIND_EXAMINATION)
                .param("searchLabel", SEARCH_LABEL)
                .param("searchDate", SEARCH_DATE_BAD_FORMAT)
                .param("searchStartTime", SEARCH_START_TIME)
                .param("searchEndTime", SEARCH_END_TIME)
                .header("Authorization", accessToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAssignRoom_Success() throws Exception {
        AssignExaminationDTO examinationDTO = new AssignExaminationDTO();
        examinationDTO.setId(EXAMINATION_1_ID);
        examinationDTO.setKind(KIND_EXAMINATION);
        examinationDTO.setRoomId(ROOM_1_ID);
        examinationDTO.setLabel(ROOM_1_LABEL);
        examinationDTO.setAvailable(AVAILABLE_ROOM);

        String jsonBody = TestUtil.json(examinationDTO);

        mockMvc.perform(put(URL_PREFIX + "/assign")
                .header("Authorization", accessToken)
                .contentType(contentType)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(ROOM_1_ID.intValue()))
                .andExpect(jsonPath("$.label").value(ROOM_1_LABEL))
                .andExpect(jsonPath("$.kind").value(KIND_EXAMINATION));
    }

    @Test
    public void testAssignRoom_BadRequest() throws Exception {
        AssignExaminationDTO examinationDTO = new AssignExaminationDTO();
        examinationDTO.setId(EXAMINATION_2_ID);
        examinationDTO.setKind(KIND_EXAMINATION);
        examinationDTO.setRoomId(ROOM_2_ID);
        examinationDTO.setLabel(ROOM_2_LABEL);
        examinationDTO.setAvailable(AVAILABLE_ROOM);

        String jsonBody = TestUtil.json(examinationDTO);

        mockMvc.perform(put(URL_PREFIX + "/assign")
                .header("Authorization", accessToken)
                .contentType(contentType)
                .content(jsonBody))
                .andExpect(status().isBadRequest());
    }
}
