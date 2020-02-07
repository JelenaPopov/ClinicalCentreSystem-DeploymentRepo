package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.response.RequestForTimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffType;
import ftn.tim16.ClinicalCentreSystem.model.*;
import ftn.tim16.ClinicalCentreSystem.repository.TimeOffNurseRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ftn.tim16.ClinicalCentreSystem.constants.TimeOffNurseConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class TimeOffNurseServiceUnitTests {

    @Autowired
    private TimeOffNurseService timeOffNurseService;

    @MockBean
    private TimeOffNurseRepository timeOffNurseRepositoryMocked;

    @MockBean
    private EmailNotificationService emailNotificationServiceMocked;

    @Test
    public void testFindByNurseClinicIdAndStatus() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        Nurse nurse = new Nurse(NEW_NURSE_EMAIL, NEW_NURSE_PASSWORD, NEW_NURSE_FIRST_NAME, NEW_NURSE_lAST_NAME, NEW_NURSE_PHONE_NUMBER,
                LocalTime.parse(NEW_NURSE_WORK_HOURS_FROM), LocalTime.parse(NEW_NURSE_WORK_HOURS_TO), clinic, getAuthority(NEW_NURSE_AUTHORITY));

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_TO, HOUR, MIN, SEC);

        TimeOffNurse timeOffNurse1 = new TimeOffNurse(TIME_OFF, new DateTimeInterval(startDate, endDate), AWAITING, nurse);
        timeOffNurse1.setId(ID);

        TimeOffNurse timeOffNurse2 = new TimeOffNurse(TIME_OFF, new DateTimeInterval(startDate.plusDays(2), endDate.plusDays(4)), AWAITING, nurse);
        timeOffNurse2.setId(ID + 1);

        List<TimeOffNurse> requestForTimeOffDTOS = new ArrayList<>();
        requestForTimeOffDTOS.add(timeOffNurse1);
        requestForTimeOffDTOS.add(timeOffNurse2);

        Mockito.when(timeOffNurseRepositoryMocked.findByNurseClinicIdAndStatus(ID, TIME_OFF_STATUS)).thenReturn(requestForTimeOffDTOS);
        List<RequestForTimeOffDTO> timeOffDoctorResults = timeOffNurseService.getRequestsForHolidayOrTimeOff(ID);
        Assert.assertNotNull(timeOffDoctorResults);
        Assert.assertEquals(SERVICE_AWAITING_COUNT, timeOffDoctorResults.size());
        for (RequestForTimeOffDTO requestForTimeOffDTO : timeOffDoctorResults) {
            Assert.assertEquals(AWAITING, requestForTimeOffDTO.getStatus());
        }
        verify(timeOffNurseRepositoryMocked, times(1)).findByNurseClinicIdAndStatus(ID, TIME_OFF_STATUS);
    }

    @Test
    public void testApproveRequestForHolidayOrTimeOff_timeOffNurse_doesNotExist() {

        Mockito.when(timeOffNurseRepositoryMocked.findByIdAndStatus(APPROVED_TIME_OFF, TIME_OFF_STATUS)).thenReturn(null);
        Assert.assertNull(timeOffNurseService.approveRequestForHolidayOrTimeOff(APPROVED_TIME_OFF));

        verify(timeOffNurseRepositoryMocked, times(1)).findByIdAndStatus(APPROVED_TIME_OFF, TIME_OFF_STATUS);
    }

    @Test
    public void testApproveRequestForHolidayOrTimeOff_success() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        Nurse nurse = new Nurse(NEW_NURSE_EMAIL, NEW_NURSE_PASSWORD, NEW_NURSE_FIRST_NAME, NEW_NURSE_lAST_NAME, NEW_NURSE_PHONE_NUMBER,
                LocalTime.parse(NEW_NURSE_WORK_HOURS_FROM), LocalTime.parse(NEW_NURSE_WORK_HOURS_TO), clinic, getAuthority(NEW_NURSE_AUTHORITY));

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_TO, HOUR, MIN, SEC);

        TimeOffNurse timeOffNurse = new TimeOffNurse(TIME_OFF, new DateTimeInterval(startDate, endDate), AWAITING, nurse);
        timeOffNurse.setId(ID);

        TimeOffNurse timeOffNurseSaved = new TimeOffNurse(TIME_OFF, new DateTimeInterval(startDate, endDate), TimeOffStatus.APPROVED, nurse);
        timeOffNurseSaved.setId(ID);

        RequestForTimeOffDTO requestForTimeOffDTO = new RequestForTimeOffDTO(timeOffNurseSaved);

        Mockito.when(timeOffNurseRepositoryMocked.findByIdAndStatus(AWAITING_TIME_OFF, TIME_OFF_STATUS)).thenReturn(timeOffNurse);
        Mockito.when(timeOffNurseRepositoryMocked.save(timeOffNurseSaved)).thenReturn(timeOffNurseSaved);
        Mockito.doNothing().when(emailNotificationServiceMocked).sendEmail(anyString(), anyString(), anyString());

        RequestForTimeOffDTO timeOffNurseResult = timeOffNurseService.approveRequestForHolidayOrTimeOff(AWAITING_TIME_OFF);

        assertNotNull(timeOffNurseResult);
        assertEquals(requestForTimeOffDTO.getId(), timeOffNurseResult.getId());
        assertEquals(requestForTimeOffDTO.getStatus(), timeOffNurseResult.getStatus());
        verify(timeOffNurseRepositoryMocked, times(1)).findByIdAndStatus(AWAITING_TIME_OFF, TIME_OFF_STATUS);
        verify(timeOffNurseRepositoryMocked, times(1)).save(ArgumentMatchers.any(TimeOffNurse.class));
    }

    @Test
    public void testRejectRequestForHolidayOrTimeOff_timeOffNurse_doesNotExist() {

        Mockito.when(timeOffNurseRepositoryMocked.findByIdAndStatus(APPROVED_TIME_OFF, TIME_OFF_STATUS)).thenReturn(null);
        Assert.assertNull(timeOffNurseService.rejectRequestForHolidayOrTimeOff(APPROVED_TIME_OFF, REASON_FOR_REJECTION));

        verify(timeOffNurseRepositoryMocked, times(1)).findByIdAndStatus(APPROVED_TIME_OFF, TIME_OFF_STATUS);
    }

    @Test
    public void testRejectRequestForHolidayOrTimeOff_success() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        Nurse nurse = new Nurse(NEW_NURSE_EMAIL, NEW_NURSE_PASSWORD, NEW_NURSE_FIRST_NAME, NEW_NURSE_lAST_NAME, NEW_NURSE_PHONE_NUMBER,
                LocalTime.parse(NEW_NURSE_WORK_HOURS_FROM), LocalTime.parse(NEW_NURSE_WORK_HOURS_TO), clinic, getAuthority(NEW_NURSE_AUTHORITY));

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_TO, HOUR, MIN, SEC);

        TimeOffNurse timeOffNurse = new TimeOffNurse(TimeOffType.TIME_OFF, new DateTimeInterval(startDate, endDate), AWAITING, nurse);
        timeOffNurse.setId(ID);

        TimeOffNurse timeOffNurseSaved = new TimeOffNurse(TimeOffType.HOLIDAY, new DateTimeInterval(startDate, endDate), REJECTED, nurse);
        timeOffNurseSaved.setId(ID);

        RequestForTimeOffDTO requestForTimeOffDTO = new RequestForTimeOffDTO(timeOffNurseSaved);

        Mockito.when(timeOffNurseRepositoryMocked.findByIdAndStatus(AWAITING_TIME_OFF, TIME_OFF_STATUS)).thenReturn(timeOffNurse);
        Mockito.when(timeOffNurseRepositoryMocked.save(timeOffNurseSaved)).thenReturn(timeOffNurseSaved);
        Mockito.doNothing().when(emailNotificationServiceMocked).sendEmail(anyString(), anyString(), anyString());

        RequestForTimeOffDTO timeOffNurseResult = timeOffNurseService.rejectRequestForHolidayOrTimeOff(AWAITING_TIME_OFF, REASON_FOR_REJECTION);

        assertNotNull(timeOffNurseResult);
        assertEquals(requestForTimeOffDTO.getId(), timeOffNurseResult.getId());
        assertEquals(requestForTimeOffDTO.getStatus(), timeOffNurseResult.getStatus());

        verify(timeOffNurseRepositoryMocked, times(1)).findByIdAndStatus(AWAITING_TIME_OFF, TIME_OFF_STATUS);
        verify(timeOffNurseRepositoryMocked, times(1)).save(ArgumentMatchers.any(TimeOffNurse.class));
    }

    private Set<Authority> getAuthority(String role) {
        Authority authority = new Authority();
        authority.setName(role);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);

        return authorities;
    }

}
