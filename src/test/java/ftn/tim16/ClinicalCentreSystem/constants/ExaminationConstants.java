package ftn.tim16.ClinicalCentreSystem.constants;

import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationKind;
import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class ExaminationConstants {
    public static final String NEW_CLINIC_NAME = "VMA klinika";

    public static final String NEW_CLINIC_ADDRESS = "Novi Sad Tolstojeva";

    public static final String NEW_CLINIC_DESCRIPTION = "VMA Novi Sad";

    public static final Long CLINIC_ID = 1L;

    public static final Long EXAMINATION_ID = 1L;

    public static final Long EXAMINATION_6_ID = 6L;

    public static final String DATE = "2020-12-12";

    public static final String DATE_2021 = "2021-02-20";

    public static final int YEAR = 2020;

    public static final int MONTH = 12;

    public static final int DAY_OF_MONTH = 12;

    public static final int START_TIME_HOUR = 8;

    public static final int END_TIME_HOUR = 11;

    public static final String END_TIME_HOUR_2 = "08:00";

    public static final int MIN = 00;

    public static final int SEC = 00;

    public static final ExaminationKind EXAMINATION = ExaminationKind.EXAMINATION;

    public static final ExaminationKind OPERATION = ExaminationKind.OPERATION;

    public static final ExaminationStatus EXAMINATION_STATUS_APPROVED = ExaminationStatus.APPROVED;

    public static final ExaminationStatus EXAMINATION_STATUS_AWAITING = ExaminationStatus.AWAITING;

    public static final Integer DISCOUNT = 0;

    public static final String SEARCH_START_TIME = "09:00";

    public static final ExaminationStatus EXAMINATION_STATUS_CANCELED = ExaminationStatus.CANCELED;

    public static final Pageable PAGE = PageRequest.of(0, 5);

    public static final String NEW_NURSE_EMAIL = "mirosla.simic@maildrop.cc";

    public static final String NEW_NURSE_PASSWORD = "$2a$10$dT.7xoRoI338DdVr0E19EOj4/xrYSFyRLF6CAWphlBEONNnX22WfK";

    public static final String NEW_NURSE_FIRST_NAME = "Vlastimir";

    public static final String NEW_NURSE_lAST_NAME = "Lazarevic";

    public static final String NEW_NURSE_PHONE_NUMBER = "062356123";

    public static final String NEW_NURSE_WORK_HOURS_FROM = "08:00:00";

    public static final String NEW_NURSE_WORK_HOURS_TO = "22:00:00";

    public static final String NEW_NURSE_AUTHORITY = "NURSE";

    public static final String ROOM_LABEL_1 = "VMA";

    public static final Long ROOM_1_ID = 1L;

    public static final Long ROOM_3_ID = 3L;

    public static final ExaminationStatus AWAITING = ExaminationStatus.AWAITING;

    public static final Long CLINIC_ADMIN_ID = 1L;

    public static final Long CLINIC_ADMIN_2_ID = 2L;

    public static final String CLINIC_ADMIN_EMAIL = "ClinicAdmin1@maildrop.cc";

    public static final String CLINIC_ADMIN_PASSWORD = "$2a$10$O2sRY6wvf0lpUu/mF5RN2u9dSW3AAzEplF4g9RpzwDOfSCFRhad6C";

    public static final String CLINIC_ADMIN_FIRST_NAME = "Marko";

    public static final String CLINIC_ADMIN_LAST_NAME = "Marković";

    public static final String CLINIC_ADMIN_PHONE_NUMBER = "064153456";

    public static final String CLINIC_ADMIN_ROLE = "ROLE_CLINIC_ADMIN";

    public static final int NUMBER_OF_APPROVED_EXAMINATION_IN_FUTURE = 2;

    public static final int NUMBER_OF_APPROVED_EXAMINATION_ON_DATE = 2;

    public static final int NUMBER_OF_APPROVED_EXAMINATION_ON_DATE_EMPTY = 0;

    public static final int NUMBER_OF_AWAITING_EXAMINATION_IN_FUTURE_CLINIC_ADMIN_2 = 0;

    public static final int NUMBER_OF_AWAITING_OPERATION_IN_FUTURE_CLINIC_ADMIN_2 = 1;

    public static final String KIND_EXAMINATION = "EXAMINATION";
    public static final Integer PAGE_NUMBER = 0;
    public static final Integer PAGE_SIZE = 5;
    public static final Integer LIST_EXAMINATIONS_COUNT = 2;
}
