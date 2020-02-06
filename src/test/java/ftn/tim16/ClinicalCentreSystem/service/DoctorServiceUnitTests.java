package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.constants.RoomConstants;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.DoctorDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.DoctorStatus;
import ftn.tim16.ClinicalCentreSystem.model.*;
import ftn.tim16.ClinicalCentreSystem.repository.DoctorRepository;
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

import static ftn.tim16.ClinicalCentreSystem.constants.DoctorConstants.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DoctorServiceUnitTests {

    @Autowired
    private DoctorService doctorService;

    @MockBean
    private DoctorRepository doctorRepositoryMocked;

    @MockBean
    private TimeOffDoctorService timeOffDoctorServiceMocked;

    @MockBean
    private ExaminationService examinationServiceMocked;

    @Test
    public void testHaveToChangeDoctor() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_LAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, null,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));
        doctor.setId(DOCTOR_ID);

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, null, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        examination.setId(EXAMINATION_ID);

        Mockito.when(timeOffDoctorServiceMocked.isDoctorOnVacation(DOCTOR_ID, startDate, endDate)).thenReturn(false);
        Mockito.when(examinationServiceMocked.getDoctorExaminationsOnDay(DOCTOR_ID, startDate)).thenReturn(new ArrayList<>());

        Assert.assertTrue(doctorService.haveToChangeDoctor(examination, doctor, startDate, endDate));

        verify(timeOffDoctorServiceMocked, times(1)).isDoctorOnVacation
                (DOCTOR_ID, startDate, endDate);
        verify(examinationServiceMocked, times(1)).getDoctorExaminationsOnDay
                (DOCTOR_ID, startDate);
    }

    @Test
    public void testGetAvailableDoctor() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);
        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_LAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));
        doctor.setId(DOCTOR_ID);

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, examinationType, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        examination.setId(EXAMINATION_ID);

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);

        Mockito.when(doctorRepositoryMocked.findByClinicIdAndSpecializedIdAndStatusNot(CLINIC_ID, examinationType.getId(), DOCTOR_STATUS_DELETED)).thenReturn(doctors);
        Mockito.when(timeOffDoctorServiceMocked.isDoctorOnVacation(DOCTOR_ID, startDate, endDate)).thenReturn(false);
        Mockito.when(examinationServiceMocked.getDoctorExaminationsOnDay(DOCTOR_ID, startDate)).thenReturn(new ArrayList<>());

        Doctor doctorResult = doctorService.getAvailableDoctor(examinationType, startDate, endDate, CLINIC_ID);

        Assert.assertNotNull(doctorResult);
        Assert.assertEquals(DOCTOR_ID, doctorResult.getId());

        verify(doctorRepositoryMocked, times(1)).findByClinicIdAndSpecializedIdAndStatusNot(CLINIC_ID, examinationType.getId(), DOCTOR_STATUS_DELETED);
        verify(timeOffDoctorServiceMocked, times(1)).isDoctorOnVacation(DOCTOR_ID, startDate, endDate);
        verify(examinationServiceMocked, times(1)).getDoctorExaminationsOnDay
                (DOCTOR_ID, startDate);
    }

    @Test
    public void testGetAllAvailableDoctors() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);
        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_LAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));
        doctor.setId(DOCTOR_ID);

        Doctor doctor2 = new Doctor(NEW_DOCTOR_2_EMAIL, NEW_DOCTOR_2_PASSWORD, NEW_DOCTOR_2_FIRST_NAME, NEW_DOCTOR_2_LAST_NAME, NEW_DOCTOR_2_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_2_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_2_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));
        doctor2.setId(DOCTOR_ID + 1);

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR_10, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, examinationType, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        examination.setId(EXAMINATION_ID);

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);
        doctors.add(doctor2);

        Mockito.when(doctorRepositoryMocked.findByStatusNotAndClinicIdAndSpecializedId(DOCTOR_STATUS_DELETED, CLINIC_ID, EXAMINATION_TYPE_ID)).thenReturn(doctors);
        Mockito.when(timeOffDoctorServiceMocked.isDoctorOnVacation(DOCTOR_ID, startDate, endDate)).thenReturn(false);
        Mockito.when(examinationServiceMocked.getDoctorExaminationsOnDay(DOCTOR_ID, startDate)).thenReturn(new ArrayList<>());

        List<DoctorDTO> doctorsResult = doctorService.getAllAvailableDoctors(EXAMINATION_TYPE_ID, CLINIC_ID, START_DATE_TIME, END_DATE_TIME);

        Assert.assertNotNull(doctorsResult);
        Assert.assertEquals(doctors.size(), doctorsResult.size());

        verify(doctorRepositoryMocked, times(1)).findByStatusNotAndClinicIdAndSpecializedId(DOCTOR_STATUS_DELETED, CLINIC_ID, EXAMINATION_TYPE_ID);
        verify(timeOffDoctorServiceMocked, times(1)).isDoctorOnVacation(DOCTOR_ID, startDate, endDate);
        verify(examinationServiceMocked, times(1)).getDoctorExaminationsOnDay
                (DOCTOR_ID, startDate);
    }

    @Test
    public void testGetAllAvailableDoctors_oneDoctorIsNotAvailable() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);
        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_LAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));
        doctor.setId(DOCTOR_ID);

        Doctor doctor2 = new Doctor(NEW_DOCTOR_2_EMAIL, NEW_DOCTOR_2_PASSWORD, NEW_DOCTOR_2_FIRST_NAME, NEW_DOCTOR_2_LAST_NAME, NEW_DOCTOR_2_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_2_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_2_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));
        doctor2.setId(DOCTOR_ID + 1);

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR_10, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, examinationType, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        examination.setId(EXAMINATION_ID);

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);
        doctors.add(doctor2);

        List<Examination> examinations = new ArrayList<>();
        examinations.add(examination);
        doctor.getExaminations().add(examination);

        Mockito.when(doctorRepositoryMocked.findByStatusNotAndClinicIdAndSpecializedId(DOCTOR_STATUS_DELETED, CLINIC_ID, EXAMINATION_TYPE_ID)).thenReturn(doctors);
        Mockito.when(timeOffDoctorServiceMocked.isDoctorOnVacation(DOCTOR_ID, startDate, endDate)).thenReturn(false);
        Mockito.when(timeOffDoctorServiceMocked.isDoctorOnVacation(DOCTOR_ID + 1, startDate, endDate)).thenReturn(false);
        Mockito.when(examinationServiceMocked.getDoctorExaminationsOnDay(DOCTOR_ID, startDate)).thenReturn(examinations);
        Mockito.when(examinationServiceMocked.getDoctorExaminationsOnDay(DOCTOR_ID + 1, startDate)).thenReturn(new ArrayList<>());

        List<DoctorDTO> doctorsResult = doctorService.getAllAvailableDoctors(EXAMINATION_TYPE_ID, CLINIC_ID, START_DATE_TIME, END_DATE_TIME);

        Assert.assertNotNull(doctorsResult);
        Assert.assertEquals(doctors.size() - 1, doctorsResult.size());

        verify(doctorRepositoryMocked, times(1)).findByStatusNotAndClinicIdAndSpecializedId(DOCTOR_STATUS_DELETED, CLINIC_ID, EXAMINATION_TYPE_ID);
        verify(timeOffDoctorServiceMocked, times(1)).isDoctorOnVacation(DOCTOR_ID, startDate, endDate);
        verify(timeOffDoctorServiceMocked, times(1)).isDoctorOnVacation(DOCTOR_ID + 1, startDate, endDate);
        verify(examinationServiceMocked, times(1)).getDoctorExaminationsOnDay
                (DOCTOR_ID, startDate);
        verify(examinationServiceMocked, times(1)).getDoctorExaminationsOnDay
                (DOCTOR_ID + 1, startDate);
    }

    @Test
    public void testFindById() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);
        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_LAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));
        doctor.setId(DOCTOR_ID);

        List<DoctorStatus> doctorStatuses = new ArrayList<>();
        doctorStatuses.add(NEVER_LOGGED_IN);
        doctorStatuses.add(ACTIVE);

        Mockito.when(doctorRepositoryMocked.findByIdAndStatusIn(DOCTOR_ID, doctorStatuses)).thenReturn(doctor);

        Doctor doctorResult = doctorService.findById(DOCTOR_ID);

        Assert.assertNotNull(doctorResult);
        Assert.assertEquals(DOCTOR_ID, doctorResult.getId());

        verify(doctorRepositoryMocked, times(1)).findByIdAndStatusIn(DOCTOR_ID, doctorStatuses);

    }

    @Test
    public void testIsAvailable_error() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);
        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_LAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));
        doctor.setId(DOCTOR_ID);
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR_06, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Assert.assertFalse(doctorService.isAvailable(doctor, startDate, endDate));
    }

    @Test
    public void testIsAvailable_success() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);
        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_LAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));
        doctor.setId(DOCTOR_ID);
        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Mockito.when(timeOffDoctorServiceMocked.isDoctorOnVacation(DOCTOR_ID, startDate, endDate)).thenReturn(false);
        Mockito.when(examinationServiceMocked.getDoctorExaminationsOnDay(DOCTOR_ID, startDate)).thenReturn(new ArrayList<>());

        Assert.assertTrue(doctorService.isAvailable(doctor, startDate, endDate));
        verify(timeOffDoctorServiceMocked, times(1)).isDoctorOnVacation(DOCTOR_ID, startDate, endDate);
        verify(examinationServiceMocked, times(1)).getDoctorExaminationsOnDay
                (DOCTOR_ID, startDate);
    }

    @Test
    public void testIsAvailable_error_DoctorHasExamination() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(CLINIC_ID);
        ExaminationType examinationType = new ExaminationType(NEW_EXAMINATION_TYPE_LABEL, NEW_EXAMINATION_TYPE_PRICE, clinic, EXISTING);
        Doctor doctor = new Doctor(NEW_DOCTOR_EMAIL, NEW_DOCTOR_PASSWORD, NEW_DOCTOR_FIRST_NAME, NEW_DOCTOR_LAST_NAME, NEW_DOCTOR_PHONE_NUMBER,
                LocalTime.parse(NEW_DOCTOR_WORK_HOURS_FROM), LocalTime.parse(NEW_DOCTOR_WORK_HOURS_TO), clinic, examinationType,
                ACTIVE, getAuthority(NEW_DOCTOR_AUTHORITY));
        doctor.setId(DOCTOR_ID);

        LocalDateTime startDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR, MIN, SEC);
        LocalDateTime endDate = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, END_TIME_HOUR, MIN, SEC);

        Examination examination = new Examination(EXAMINATION, new DateTimeInterval(startDate, endDate),
                EXAMINATION_STATUS_AWAITING, examinationType, null, DISCOUNT, null, clinic, getLoggedInClinicAdmin());
        examination.setId(EXAMINATION_ID);

        List<Examination> examinations = new ArrayList<>();
        examinations.add(examination);
        doctor.getExaminations().add(examination);

        LocalDateTime startDateTimeForNewExamination = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, START_TIME_HOUR_9, MIN, SEC);

        Mockito.when(timeOffDoctorServiceMocked.isDoctorOnVacation(DOCTOR_ID, startDateTimeForNewExamination, endDate)).thenReturn(false);
        Mockito.when(examinationServiceMocked.getDoctorExaminationsOnDay(DOCTOR_ID, startDateTimeForNewExamination)).thenReturn(examinations);

        Assert.assertFalse(doctorService.isAvailable(doctor, startDateTimeForNewExamination, endDate));
        verify(timeOffDoctorServiceMocked, times(1)).isDoctorOnVacation(DOCTOR_ID, startDateTimeForNewExamination, endDate);
        verify(examinationServiceMocked, times(1)).getDoctorExaminationsOnDay
                (DOCTOR_ID, startDateTimeForNewExamination);
    }

    private ClinicAdministrator getLoggedInClinicAdmin() {
        Clinic clinic = new Clinic(NEW_CLINIC_NAME, NEW_CLINIC_ADDRESS, NEW_CLINIC_DESCRIPTION);
        clinic.setId(RoomConstants.CLINIC_ID);
        ClinicAdministrator clinicAdministrator = new ClinicAdministrator(CLINIC_ADMIN_EMAIL, CLINIC_ADMIN_PASSWORD,
                CLINIC_ADMIN_FIRST_NAME, CLINIC_ADMIN_LAST_NAME, CLINIC_ADMIN_PHONE_NUMBER, clinic, getClinicAdminAuthority());
        clinicAdministrator.setId(CLINIC_ADMIN_ID);

        return clinicAdministrator;
    }

    private Set<Authority> getAuthority(String role) {
        Authority authority = new Authority();
        authority.setName(role);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);

        return authorities;
    }

    private Set<Authority> getClinicAdminAuthority() {
        Authority clinicAdminAuthority = new Authority();
        clinicAdminAuthority.setName(CLINIC_ADMIN_ROLE);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(clinicAdminAuthority);

        return authorities;
    }
}
