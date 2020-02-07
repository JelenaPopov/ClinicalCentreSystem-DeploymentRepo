package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.TestUtil;
import ftn.tim16.ClinicalCentreSystem.dto.request.AssignExaminationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.RoomDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.RoomWithIdDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.LoggedInUserDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.RoomPagingDTO;
import ftn.tim16.ClinicalCentreSystem.model.Clinic;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;
import ftn.tim16.ClinicalCentreSystem.security.auth.JwtAuthenticationRequest;
import ftn.tim16.ClinicalCentreSystem.service.RoomService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static ftn.tim16.ClinicalCentreSystem.constants.RoomConstants.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class RoomControllerUnitTests {

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

    @MockBean
    private RoomService roomServiceMock;

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
        Pageable page = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        List<RoomDTO> roomDTOList = new ArrayList<>();
        roomDTOList.add(new RoomDTO(ROOM_1_ID, ROOM_1_LABEL, KIND_EXAMINATION));
        roomDTOList.add(new RoomDTO(ROOM_2_ID, ROOM_2_LABEL, KIND_EXAMINATION));
        RoomPagingDTO roomPagingDTO = new RoomPagingDTO(roomDTOList, DB_ROOMS_COUNT);

        when(roomServiceMock.searchRoomsInClinic(KIND_EXAMINATION, getClinic(), page, SEARCH_LABEL,
                SEARCH_DATE, SEARCH_START_TIME, SEARCH_END_TIME)).thenReturn(roomPagingDTO);

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
                .andExpect(jsonPath("$.roomDTOList").value(hasSize(LIST_ROOMS_COUNT)))
                .andExpect(jsonPath("$.roomDTOList.[*].id").value(hasItem(ROOM_1_ID.intValue())))
                .andExpect(jsonPath("$.roomDTOList.[*].id").value(hasItem(ROOM_2_ID.intValue())))
                .andExpect(jsonPath("$.numberOfItems").value(DB_ROOMS_COUNT));

        verify(roomServiceMock, times(1))
                .searchRoomsInClinic(KIND_EXAMINATION, getClinic(), page, SEARCH_LABEL, SEARCH_DATE, SEARCH_START_TIME, SEARCH_END_TIME);
    }

    @Test
    public void testSearchRoomsInClinic_BadRequest() throws Exception {
        Pageable page = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        when(roomServiceMock.searchRoomsInClinic(KIND_EXAMINATION, getClinic(), page, SEARCH_LABEL,
                SEARCH_DATE_BAD_FORMAT, SEARCH_START_TIME, SEARCH_END_TIME)).thenThrow(DateTimeParseException.class);

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

        verify(roomServiceMock, times(1))
                .searchRoomsInClinic(KIND_EXAMINATION, getClinic(), page, SEARCH_LABEL, SEARCH_DATE_BAD_FORMAT, SEARCH_START_TIME, SEARCH_END_TIME);
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

        RoomWithIdDTO assignedRoom = new RoomWithIdDTO(ROOM_1_ID, ROOM_1_LABEL, KIND_EXAMINATION);

        when(roomServiceMock.assignRoom(any(AssignExaminationDTO.class), any(ClinicAdministrator.class))).thenReturn(assignedRoom);

        mockMvc.perform(put(URL_PREFIX + "/assign")
                .header("Authorization", accessToken)
                .contentType(contentType)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(ROOM_1_ID.intValue()))
                .andExpect(jsonPath("$.label").value(ROOM_1_LABEL))
                .andExpect(jsonPath("$.kind").value(KIND_EXAMINATION));

        verify(roomServiceMock, times(1)).assignRoom(any(AssignExaminationDTO.class), any(ClinicAdministrator.class));
    }

    @Test
    public void testAssignRoom_BadRequest() throws Exception {
        AssignExaminationDTO examinationDTO = new AssignExaminationDTO();
        examinationDTO.setId(EXAMINATION_2_ID);
        examinationDTO.setKind(KIND_EXAMINATION);
        examinationDTO.setRoomId(ROOM_1_ID);
        examinationDTO.setLabel(ROOM_1_LABEL);
        examinationDTO.setAvailable(AVAILABLE_ROOM);

        String jsonBody = TestUtil.json(examinationDTO);

        when(roomServiceMock.assignRoom(any(AssignExaminationDTO.class), any(ClinicAdministrator.class))).thenReturn(null);

        mockMvc.perform(put(URL_PREFIX + "/assign")
                .header("Authorization", accessToken)
                .contentType(contentType)
                .content(jsonBody))
                .andExpect(status().isBadRequest());

        verify(roomServiceMock, times(1)).assignRoom(any(AssignExaminationDTO.class), any(ClinicAdministrator.class));
    }

    private Clinic getClinic() {
        Clinic clinic = new Clinic(CLINIC_NAME, CLINIC_DESCRIPTION, CLINIC_ADDRESS);
        clinic.setId(CLINIC_ID);

        return clinic;
    }

}