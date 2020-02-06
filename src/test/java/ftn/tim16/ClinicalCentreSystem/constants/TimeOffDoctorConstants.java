package ftn.tim16.ClinicalCentreSystem.constants;

import ftn.tim16.ClinicalCentreSystem.enumeration.DoctorStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.LogicalStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffType;

public class TimeOffDoctorConstants {

    public static final int DB_AWAITING_COUNT = 2;

    public static final Long DOCTOR_ID_WITH_AWAITING = 2L;

    public static final Long CLINIC_ID = 1L;

    public static final Long DOCTOR_ID_WITHOUT_AWAITING = 1L;

    public static final TimeOffStatus TIME_OFF_STATUS_AWAITING = TimeOffStatus.AWAITING;

    public static final Long APPROVED_TIME_OFF = 1L;

    public static final Long AWAITING_TIME_OFF = 2L;

    public static final String NEW_DOCTOR_EMAIL = "miroslav.simic@maildrop.cc";

    public static final String NEW_DOCTOR_PASSWORD = "$2a$10$dT.7xoRoI338DdVr0E19EOj4/xrYSFyRLF6CAWphlBEONNnX22WfK";

    public static final String NEW_DOCTOR_FIRST_NAME = "Vlastimir";

    public static final String NEW_DOCTOR_lAST_NAME = "Lazarevic";

    public static final String NEW_DOCTOR_PHONE_NUMBER = "068356123";

    public static final String NEW_DOCTOR_WORK_HOURS_FROM = "08:00:00";

    public static final String NEW_DOCTOR_WORK_HOURS_TO = "22:00:00";

    public static final String NEW_DOCTOR_AUTHORITY = "DOCTOR";

    public static final String NEW_CLINIC_NAME = "VMA klinika";

    public static final String NEW_CLINIC_ADDRESS = "Novi Sad Tolstojeva";

    public static final String NEW_CLINIC_DESCRIPTION = "VMA Novi Sad";

    public static final String NEW_EXAMINATION_TYPE_LABEL = "Opsta praksa i hirurgija";

    public static final Double NEW_EXAMINATION_TYPE_PRICE = 1000.0;

    public static final String REASON_FOR_REJECTION = "At that time we have a lot of work so you have to work.";

    public static final Long ID = 1L;

    public static final TimeOffStatus REJECTED = TimeOffStatus.REJECTED;

    public static final TimeOffStatus AWAITING = TimeOffStatus.AWAITING;

    public static final TimeOffStatus APPROVED = TimeOffStatus.APPROVED;

    public static final LogicalStatus EXISTING = LogicalStatus.EXISTING;

    public static final DoctorStatus ACTIVE = DoctorStatus.ACTIVE;

    public static final TimeOffType HOLIDAY = TimeOffType.HOLIDAY;

    public static final TimeOffType TIME_OFF = TimeOffType.TIME_OFF;

    public static final int YEAR = 2020;

    public static final int MONTH = 7;

    public static final int DAY_OF_MONTH = 17;

    public static final int DAY_OF_MONTH_TO = 27;

    public static final int HOUR = 8;

    public static final int MIN = 00;

    public static final int SEC = 00;

    public static final String DB_DOCTOR_FIRST_NAME = "Miodrag";

    public static final String DB_DOCTOR_LAST_NAME = "Lazarevic";

    public static final int SERVICE_AWAITING_COUNT = 1;

    public static final int DB_SERVICE_AWAITING_COUNT = 2;

    public static final Long DB_AWAITING_TIME_OFF = 2L;

    public static final Long DB_APPROVED_TIME_OFF = 1L;

}
