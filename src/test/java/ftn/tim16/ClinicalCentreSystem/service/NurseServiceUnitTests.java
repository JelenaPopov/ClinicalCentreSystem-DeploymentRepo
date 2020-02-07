package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.constants.RoomConstants;
import ftn.tim16.ClinicalCentreSystem.model.*;
import ftn.tim16.ClinicalCentreSystem.repository.NurseRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static ftn.tim16.ClinicalCentreSystem.constants.NurseConstants.CLINIC_ID;
import static ftn.tim16.ClinicalCentreSystem.constants.NurseConstants.NEW_CLINIC_ADDRESS;
import static ftn.tim16.ClinicalCentreSystem.constants.NurseConstants.NEW_CLINIC_DESCRIPTION;
import static ftn.tim16.ClinicalCentreSystem.constants.NurseConstants.NEW_CLINIC_NAME;
import static ftn.tim16.ClinicalCentreSystem.constants.RoomConstants.NEW_NURSE_AUTHORITY;
import static ftn.tim16.ClinicalCentreSystem.constants.RoomConstants.NEW_NURSE_EMAIL;
import static ftn.tim16.ClinicalCentreSystem.constants.RoomConstants.NEW_NURSE_FIRST_NAME;
import static ftn.tim16.ClinicalCentreSystem.constants.RoomConstants.NEW_NURSE_PASSWORD;
import static ftn.tim16.ClinicalCentreSystem.constants.RoomConstants.NEW_NURSE_PHONE_NUMBER;
import static ftn.tim16.ClinicalCentreSystem.constants.RoomConstants.NEW_NURSE_WORK_HOURS_FROM;
import static ftn.tim16.ClinicalCentreSystem.constants.RoomConstants.NEW_NURSE_WORK_HOURS_TO;
import static ftn.tim16.ClinicalCentreSystem.constants.RoomConstants.NEW_NURSE_lAST_NAME;
import static ftn.tim16.ClinicalCentreSystem.constants.RoomConstants.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class NurseServiceUnitTests {
    @Autowired
    private NurseService nurseService;

    @MockBean
    private NurseRepository nurseRepositoryMocked;

    @Test
    public void testGetRandomNurse() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        Nurse nurse = new Nurse(NEW_NURSE_EMAIL, NEW_NURSE_PASSWORD, NEW_NURSE_FIRST_NAME, NEW_NURSE_lAST_NAME, NEW_NURSE_PHONE_NUMBER,
                LocalTime.parse(NEW_NURSE_WORK_HOURS_FROM), LocalTime.parse(NEW_NURSE_WORK_HOURS_TO), clinic, getAuthority(NEW_NURSE_AUTHORITY));
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        examination.setId(EXAMINATION_ID);

        List<Nurse> nurses = new ArrayList<>();
        nurses.add(nurse);

        Mockito.when(nurseRepositoryMocked.findByClinicId(CLINIC_ID)).thenReturn(nurses);

        Nurse nurseResult = nurseService.getRandomNurse(EXAMINATION_ID, startDate, endDate);

        Assert.assertNotNull(nurseResult);
        Assert.assertEquals(nurse.getEmail(), nurseResult.getEmail());

        verify(nurseRepositoryMocked, times(1)).findByClinicId
                (CLINIC_ID);
    }

    private Set<Authority> getAuthority(String role) {
        Authority authority = new Authority();
        authority.setName(role);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);

        return authorities;
    }

    private ClinicAdministrator getLoggedInClinicAdmin() {
        Clinic clinic = new Clinic(RoomConstants.NEW_CLINIC_NAME, RoomConstants.NEW_CLINIC_ADDRESS, RoomConstants.NEW_CLINIC_DESCRIPTION);
        clinic.setId(RoomConstants.CLINIC_ID);
        ClinicAdministrator clinicAdministrator = new ClinicAdministrator(CLINIC_ADMIN_EMAIL, CLINIC_ADMIN_PASSWORD,
                CLINIC_ADMIN_FIRST_NAME, CLINIC_ADMIN_LAST_NAME, CLINIC_ADMIN_PHONE_NUMBER, clinic, getClinicAdminAuthority());
        clinicAdministrator.setId(CLINIC_ADMIN_ID);

        return clinicAdministrator;
    }

    private Set<Authority> getClinicAdminAuthority() {
        Authority clinicAdminAuthority = new Authority();
        clinicAdminAuthority.setName(CLINIC_ADMIN_ROLE);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(clinicAdminAuthority);

        return authorities;
    }
}
