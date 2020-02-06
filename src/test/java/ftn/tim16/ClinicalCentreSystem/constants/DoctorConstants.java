package ftn.tim16.ClinicalCentreSystem.constants;

import ftn.tim16.ClinicalCentreSystem.enumeration.DoctorStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationKind;
import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.LogicalStatus;

public class DoctorConstants {
    public static final Long NEW_DOCTOR_ID = 4L;

    public static final String NEW_DOCTOR_EMAIL = "miroslav.simic@maildrop.cc";

    public static final String NEW_DOCTOR_PASSWORD = "$2a$10$dT.7xoRoI338DdVr0E19EOj4/xrYSFyRLF6CAWphlBEONNnX22WfK";

    public static final String NEW_DOCTOR_FIRST_NAME = "Vlastimir";

    public static final String NEW_DOCTOR_LAST_NAME = "Lazarevic";

    public static final String NEW_DOCTOR_PHONE_NUMBER = "068356123";

    public static final String NEW_DOCTOR_WORK_HOURS_FROM = "08:00:00";

    public static final String NEW_DOCTOR_AUTHORITY = "DOCTOR";

    public static final String NEW_DOCTOR_WORK_HOURS_TO = "22:00:00";

    public static final Double NEW_DOCTOR_RATING = 4.2;

    public static final DoctorStatus DOCTOR_STATUS_DELETED = DoctorStatus.DELETED;

    public static final String NEW_CLINIC_NAME = "VMA klinika";

    public static final String NEW_CLINIC_ADDRESS = "Novi Sad Tolstojeva";

    public static final String NEW_CLINIC_DESCRIPTION = "VMA Novi Sad";

    public static final Long CLINIC_ID = 1L;

    public static final Long DOCTOR_ID = 1L;

    public static final DoctorStatus ACTIVE = DoctorStatus.ACTIVE;

    public static final DoctorStatus NEVER_LOGGED_IN = DoctorStatus.NEVER_LOGGED_IN;

    public static final int YEAR = 2020;

    public static final int MONTH = 12;

    public static final int DAY_OF_MONTH = 12;

    public static final int START_TIME_HOUR = 8;

    public static final int START_TIME_HOUR_9 = 9;

    public static final int START_TIME_HOUR_10 = 10;

    public static final int END_TIME_HOUR = 11;

    public static final int MIN = 00;

    public static final int SEC = 00;

    public static final ExaminationKind EXAMINATION = ExaminationKind.EXAMINATION;

    public static final ExaminationStatus EXAMINATION_STATUS_AWAITING = ExaminationStatus.AWAITING;

    public static final Integer DISCOUNT = 0;

    public static final Long EXAMINATION_ID = 1L;

    public static final Long EXAMINATION_TYPE_ID = 1L;

    public static final String NEW_EXAMINATION_TYPE_LABEL = "Opsta praksa i hirurgija";

    public static final Double NEW_EXAMINATION_TYPE_PRICE = 1000.0;

    public static final Long CLINIC_ADMIN_ID = 1L;

    public static final String CLINIC_ADMIN_EMAIL = "ClinicAdmin1@maildrop.cc";

    public static final String CLINIC_ADMIN_PASSWORD = "$2a$10$O2sRY6wvf0lpUu/mF5RN2u9dSW3AAzEplF4g9RpzwDOfSCFRhad6C";

    public static final String CLINIC_ADMIN_FIRST_NAME = "Marko";

    public static final String CLINIC_ADMIN_LAST_NAME = "Marković";

    public static final String CLINIC_ADMIN_PHONE_NUMBER = "064153456";

    public static final String CLINIC_ADMIN_ROLE = "ROLE_CLINIC_ADMIN";

    public static final LogicalStatus EXISTING = LogicalStatus.EXISTING;

    public static final int START_TIME_HOUR_06 = 06;

    public static final String START_DATE_TIME = "2020-12-12 10:00";

    public static final String END_DATE_TIME = "2020-12-12 11:00";

    public static final Long NEW_DOCTOR_2_ID = 5L;

    public static final String NEW_DOCTOR_2_EMAIL = "tamara.simic@maildrop.cc";

    public static final String NEW_DOCTOR_2_PASSWORD = "$2a$10$dT.7xoRoI338DdVr0E19EOj4/xrYSFyRLF6CAWphlBEONNnX22WfK";

    public static final String NEW_DOCTOR_2_FIRST_NAME = "Tamara";

    public static final String NEW_DOCTOR_2_LAST_NAME = "Simic";

    public static final String NEW_DOCTOR_2_PHONE_NUMBER = "069357123";

    public static final String NEW_DOCTOR_2_WORK_HOURS_FROM = "07:00:00";

    public static final String NEW_DOCTOR_2_WORK_HOURS_TO = "23:00:00";

    public static final Integer NUMBER_OF_DOCTORS_SPECIALIZED_1 = 3;

    public static final Integer NUMBER_OF_DOCTORS_SPECIALIZED_2 = 2;

    public static final Double NEW_DOCTOR_2_RATING = 3.7;

    public static final Integer DB_AVAILABLE_DOCTORS_COUNT = 3;
    public static final Integer LIST_AVAILABLE_DOCTORS_COUNT = 2;
}
