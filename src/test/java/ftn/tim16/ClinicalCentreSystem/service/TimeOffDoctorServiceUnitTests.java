package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.response.RequestForTimeOffDTO;
import ftn.tim16.ClinicalCentreSystem.model.*;
import ftn.tim16.ClinicalCentreSystem.repository.TimeOffDoctorRepository;
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

import static ftn.tim16.ClinicalCentreSystem.constants.TimeOffDoctorConstants.*;
import static ftn.tim16.ClinicalCentreSystem.constants.TimeOffNurseConstants.AWAITING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class TimeOffDoctorServiceUnitTests {

    @Autowired
    private TimeOffDoctorService timeOffDoctorService;

    @MockBean
    private TimeOffDoctorRepository timeOffDoctorRepositoryMocked;

    @MockBean
    private EmailNotificationService emailNotificationServiceMocked;

    @Test
    public void testFindByDoctorClinicIdAndStatus() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);

        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_lAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_TO, HOUR, MIN, SEC);

        TimeOffDoctor timeOffDoctor = new TimeOffDoctor(HOLIDAY, new DateTimeInterval(startDate, endDate), AWAITING, doctor);
        timeOffDoctor.setId(ID);
        List<TimeOffDoctor> requestForTimeOffDTOS = new ArrayList<>();
        requestForTimeOffDTOS.add(timeOffDoctor);

        Mockito.when(timeOffDoctorRepositoryMocked.findByDoctorClinicIdAndStatus(ID, TIME_OFF_STATUS_AWAITING)).thenReturn(requestForTimeOffDTOS);
        List<RequestForTimeOffDTO> timeOffDoctorResults = timeOffDoctorService.getRequestsForHolidayOrTimeOff(ID);
        Assert.assertNotNull(timeOffDoctorResults);
        Assert.assertEquals(SERVICE_AWAITING_COUNT, timeOffDoctorResults.size());
        for (RequestForTimeOffDTO requestForTimeOffDTO : timeOffDoctorResults) {
            Assert.assertEquals(AWAITING, requestForTimeOffDTO.getStatus());
        }
        verify(timeOffDoctorRepositoryMocked, times(1)).findByDoctorClinicIdAndStatus(ID, TIME_OFF_STATUS_AWAITING);
    }

    @Test
    public void testApproveRequestForHolidayOrTimeOff_timeOffDoctor_doesNotExist() {

        Mockito.when(timeOffDoctorRepositoryMocked.findByIdAndStatus(APPROVED_TIME_OFF, TIME_OFF_STATUS_AWAITING)).thenReturn(null);

        Assert.assertNull(timeOffDoctorService.approveRequestForHolidayOrTimeOff(APPROVED_TIME_OFF));

        verify(timeOffDoctorRepositoryMocked, times(1)).findByIdAndStatus(APPROVED_TIME_OFF, TIME_OFF_STATUS_AWAITING);
    }

    @Test
    public void testApproveRequestForHolidayOrTimeOff_success() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);

        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_lAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_TO, HOUR, MIN, SEC);

        TimeOffDoctor timeOffDoctor = new TimeOffDoctor(HOLIDAY, new DateTimeInterval(startDate, endDate), AWAITING, doctor);
        timeOffDoctor.setId(ID);

        TimeOffDoctor timeOffDoctorSaved = new TimeOffDoctor(HOLIDAY, new DateTimeInterval(startDate, endDate), APPROVED, doctor);
        timeOffDoctorSaved.setId(ID);
        RequestForTimeOffDTO requestForTimeOffDTO = new RequestForTimeOffDTO(timeOffDoctorSaved);

        Mockito.when(timeOffDoctorRepositoryMocked.findByIdAndStatus(AWAITING_TIME_OFF, TIME_OFF_STATUS_AWAITING)).thenReturn(timeOffDoctor);
        Mockito.when(timeOffDoctorRepositoryMocked.save(timeOffDoctorSaved)).thenReturn(timeOffDoctorSaved);
        Mockito.doNothing().when(emailNotificationServiceMocked).sendEmail(anyString(), anyString(), anyString());

        RequestForTimeOffDTO timeOffDoctorResult = timeOffDoctorService.approveRequestForHolidayOrTimeOff(AWAITING_TIME_OFF);

        assertNotNull(timeOffDoctorResult);
        assertEquals(requestForTimeOffDTO.getId(), timeOffDoctorResult.getId());
        assertEquals(requestForTimeOffDTO.getStatus(), timeOffDoctorResult.getStatus());

        verify(timeOffDoctorRepositoryMocked, times(1)).findByIdAndStatus(AWAITING_TIME_OFF, TIME_OFF_STATUS_AWAITING);
        verify(timeOffDoctorRepositoryMocked, times(1)).save(ArgumentMatchers.any(TimeOffDoctor.class));
    }

    @Test
    public void testRejectRequestForHolidayOrTimeOff_timeOffDoctor_doesNotExist() {

        Mockito.when(timeOffDoctorRepositoryMocked.findByIdAndStatus(APPROVED_TIME_OFF, TIME_OFF_STATUS_AWAITING)).thenReturn(null);
        Assert.assertNull(timeOffDoctorService.rejectRequestForHolidayOrTimeOff(APPROVED_TIME_OFF, REASON_FOR_REJECTION));

        verify(timeOffDoctorRepositoryMocked, times(1)).findByIdAndStatus(APPROVED_TIME_OFF, TIME_OFF_STATUS_AWAITING);
    }

    @Test
    public void testRejectRequestForHolidayOrTimeOff_success() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);

        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_lAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_TO, HOUR, MIN, SEC);

        TimeOffDoctor timeOffDoctor = new TimeOffDoctor(HOLIDAY, new DateTimeInterval(startDate, endDate), AWAITING, doctor);
        timeOffDoctor.setId(ID);

        TimeOffDoctor timeOffDoctorSaved = new TimeOffDoctor(HOLIDAY, new DateTimeInterval(startDate, endDate), REJECTED, doctor);
        timeOffDoctorSaved.setId(ID);
        RequestForTimeOffDTO requestForTimeOffDTO = new RequestForTimeOffDTO(timeOffDoctorSaved);

        Mockito.when(timeOffDoctorRepositoryMocked.findByIdAndStatus(AWAITING_TIME_OFF, TIME_OFF_STATUS_AWAITING)).thenReturn(timeOffDoctor);
        Mockito.when(timeOffDoctorRepositoryMocked.save(timeOffDoctorSaved)).thenReturn(timeOffDoctorSaved);
        Mockito.doNothing().when(emailNotificationServiceMocked).sendEmail(anyString(), anyString(), anyString());

        RequestForTimeOffDTO timeOffDoctorResult = timeOffDoctorService.rejectRequestForHolidayOrTimeOff(AWAITING_TIME_OFF, REASON_FOR_REJECTION);

        assertNotNull(timeOffDoctorResult);
        assertEquals(requestForTimeOffDTO.getId(), timeOffDoctorResult.getId());
        assertEquals(requestForTimeOffDTO.getStatus(), timeOffDoctorResult.getStatus());

        verify(timeOffDoctorRepositoryMocked, times(1)).findByIdAndStatus(AWAITING_TIME_OFF, TIME_OFF_STATUS_AWAITING);
        verify(timeOffDoctorRepositoryMocked, times(1)).save(ArgumentMatchers.any(TimeOffDoctor.class));
    }

    private Set<Authority> getAuthority(String role) {
        Authority authority = new Authority();
        authority.setName(role);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);

        return authorities;
    }


}
