package ftn.tim16.ClinicalCentreSystem.constants;

import ftn.tim16.ClinicalCentreSystem.enumeration.DoctorStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationKind;
import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.LogicalStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class RoomConstants {

    public static final Long CLINIC_ID = 1L;

    public static final Long DOCTOR_ID = 1L;

    public static final Long ROOM_1_ID = 1L;

    public static final Long ROOM_2_ID = 2L;

    public static final Long ROOM_4_ID = 4L;

    public static final Long EXAMINATION_ID = 1L;

    public static final LogicalStatus LOGICAL_STATUS_EXISTING = LogicalStatus.EXISTING;

    public static final LogicalStatus LOGICAL_STATUS_DELETED = LogicalStatus.DELETED;

    public static final String SEARCH = "A";

    public static final Pageable PAGE = PageRequest.of(0, 5);

    public static final String EMPTY_SEARCH_START_TIME = "";

    public static final String EMPTY_SEARCH_END_TIME = "";

    public static final String EMPTY_DATE = "";

    public static final String ROOM_LABEL_1 = "VMA";

    public static final String ROOM_LABEL_2 = "PANACEA";

    public static final ExaminationKind OPERATION = ExaminationKind.OPERATION;

    public static final ExaminationKind EXAMINATION = ExaminationKind.EXAMINATION;

    public static final String ALL_ROOMS = "ALL";

    public static final String EXAMINATION_ROOMS = "EXAMINATION";

    public static final String NEW_CLINIC_NAME = "VMA klinika";

    public static final String NEW_CLINIC_ADDRESS = "Novi Sad Tolstojeva";

    public static final String NEW_CLINIC_DESCRIPTION = "VMA Novi Sad";

    public static final String SEARCH_START_TIME = "09:00";

    public static final String SEARCH_END_TIME = "10:00";

    public static final String DATE = "2020-12-12";

    public static final int YEAR = 2020;

    public static final int MONTH = 12;

    public static final int DAY_OF_MONTH = 12;

    public static final int START_TIME_HOUR = 8;

    public static final int END_TIME_HOUR = 11;

    public static final int START_TIME_HOUR_11 = 11;

    public static final int END_TIME_HOUR_14 = 14;

    public static final int MIN = 00;

    public static final int SEC = 00;

    public static final ExaminationStatus EXAMINATION_STATUS_APPROVED = ExaminationStatus.APPROVED;

    public static final ExaminationStatus EXAMINATION_STATUS_AWAITING = ExaminationStatus.AWAITING;

    public static final Integer DISCOUNT = 0;

    public static final String AVAILABLE_ROOM = "2020-12-12 09:00";

    public static final String AVAILABLE_ROOM_08 = "2020-12-12 08:00";

    public static final String AVAILABLE_ROOM_11 = "2020-12-12 11:00";

    public static final String NEW_DOCTOR_EMAIL = "miroslav.simic@maildrop.cc";

    public static final String NEW_DOCTOR_PASSWORD = "$2a$10$dT.7xoRoI338DdVr0E19EOj4/xrYSFyRLF6CAWphlBEONNnX22WfK";

    public static final String NEW_DOCTOR_FIRST_NAME = "Vlastimir";

    public static final String NEW_DOCTOR_lAST_NAME = "Lazarevic";

    public static final String NEW_DOCTOR_PHONE_NUMBER = "068356123";

    public static final String NEW_DOCTOR_WORK_HOURS_FROM = "08:00:00";

    public static final LogicalStatus EXISTING = LogicalStatus.EXISTING;

    public static final String NEW_DOCTOR_WORK_HOURS_TO = "22:00:00";

    public static final String NEW_DOCTOR_AUTHORITY = "DOCTOR";

    public static final Long CLINIC_ADMIN_ID = 1L;

    public static final String CLINIC_ADMIN_EMAIL = "ClinicAdmin1@maildrop.cc";

    public static final String CLINIC_ADMIN_PASSWORD = "$2a$10$O2sRY6wvf0lpUu/mF5RN2u9dSW3AAzEplF4g9RpzwDOfSCFRhad6C";

    public static final String CLINIC_ADMIN_FIRST_NAME = "Marko";

    public static final String CLINIC_ADMIN_LAST_NAME = "Marković";

    public static final String CLINIC_ADMIN_PHONE_NUMBER = "064153456";

    public static final String CLINIC_ADMIN_ROLE = "ROLE_CLINIC_ADMIN";

    public static final String NEW_EXAMINATION_TYPE_LABEL = "Opsta praksa i hirurgija";

    public static final Double NEW_EXAMINATION_TYPE_PRICE = 1000.0;

    public static final DoctorStatus ACTIVE = DoctorStatus.ACTIVE;

    public static final String NEW_NURSE_EMAIL = "mirosla.simic@maildrop.cc";

    public static final String NEW_NURSE_PASSWORD = "$2a$10$dT.7xoRoI338DdVr0E19EOj4/xrYSFyRLF6CAWphlBEONNnX22WfK";

    public static final String NEW_NURSE_FIRST_NAME = "Vlastimir";

    public static final String NEW_NURSE_lAST_NAME = "Lazarevic";

    public static final String NEW_NURSE_PHONE_NUMBER = "062356123";

    public static final String NEW_NURSE_WORK_HOURS_FROM = "08:00:00";

    public static final String NEW_NURSE_WORK_HOURS_TO = "22:00:00";

    public static final String NEW_NURSE_AUTHORITY = "NURSE";

    public static final ExaminationStatus APPROVED = ExaminationStatus.APPROVED;

    public static final int DB_ROOM_WITH_STATUS_EXISTING_AND_CLINIC_ID_WITH_SEARCH = 0;

    public static final int DB_ROOM_WITH_STATUS_EXISTING_AND_CLINIC_ID = 5;

    public static final int DB_EXAMINATION_ROOMS = 3;

    public static final int DB_OPERATION_ROOMS = 2;

    public static final String SEARCH_ROOM = "om";

    public static final String ROOM_1_LABEL = "Room 1";

    public static final String ROOM_2_LABEL = "Room 2";

    public static final String KIND_EXAMINATION = "EXAMINATION";
    public static final Integer PAGE_NUMBER = 0;
    public static final Integer PAGE_SIZE = 5;
    public static final Integer DB_ROOMS_COUNT = 3;
    public static final Integer LIST_ROOMS_COUNT = 2;
    public static final String SEARCH_DATE_BAD_FORMAT = "20.02.2021.";

    public static final String SEARCH_LABEL = "";
    public static final String SEARCH_DATE = "2021-02-20";

    public static final Long EXAMINATION_1_ID = 1L;
    public static final Long EXAMINATION_2_ID = 2L;

    public static final String CLINIC_NAME = "Klinika mira";
    public static final String CLINIC_DESCRIPTION = "Savremena klinika u Novom Sadu";
    public static final String CLINIC_ADDRESS = "Novi sad Grbavica";
}
