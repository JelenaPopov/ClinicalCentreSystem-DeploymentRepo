package ftn.tim16.ClinicalCentreSystem.constants;

import ftn.tim16.ClinicalCentreSystem.enumeration.DoctorStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.LogicalStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffType;

public class TimeOffNurseConstants {

    public static final int DB_AWAITING_COUNT = 1;

    public static final Long NURSE_ID_WITH_AWAITING = 2L;

    public static final Long NURSE_ID_WITHOUT_AWAITING = 1L;

    public static final TimeOffStatus TIME_OFF_STATUS = TimeOffStatus.AWAITING;

    public static final Long CLINIC_ID = 1L;

    public static final Long APPROVED_TIME_OFF = 1L;

    public static final Long AWAITING_TIME_OFF = 2L;

    public static final String NEW_NURSE_EMAIL = "mirosla.simic@maildrop.cc";

    public static final String NEW_NURSE_PASSWORD = "$2a$10$dT.7xoRoI338DdVr0E19EOj4/xrYSFyRLF6CAWphlBEONNnX22WfK";

    public static final String NEW_NURSE_FIRST_NAME = "Vlastimir";

    public static final String NEW_NURSE_lAST_NAME = "Lazarevic";

    public static final String NEW_NURSE_PHONE_NUMBER = "062356123";

    public static final String NEW_NURSE_WORK_HOURS_FROM = "08:00:00";

    public static final String NEW_NURSE_WORK_HOURS_TO = "22:00:00";

    public static final String NEW_NURSE_AUTHORITY = "NURSE";

    public static final String NEW_CLINIC_NAME = "VMA";

    public static final String NEW_CLINIC_ADDRESS = "Novi Sad Tolstojeva";

    public static final String NEW_CLINIC_DESCRIPTION = "VMA Novi Sad";

    public static final String REASON_FOR_REJECTION = "At that time we have a lot of work so you have to work.";

    public static final Long ID = 1L;

    public static final TimeOffStatus REJECTED = TimeOffStatus.REJECTED;

    public static final TimeOffStatus AWAITING = TimeOffStatus.AWAITING;

    public static final TimeOffStatus APPROVED = TimeOffStatus.APPROVED;

    public static final LogicalStatus EXISTING = LogicalStatus.EXISTING;

    public static final DoctorStatus ACTIVE = DoctorStatus.ACTIVE;

    public static final TimeOffType TIME_OFF = TimeOffType.TIME_OFF;

    public static final TimeOffType HOLIDAY = TimeOffType.HOLIDAY;

    public static final int YEAR = 2020;

    public static final int MONTH = 7;

    public static final int DAY_OF_MONTH = 17;

    public static final int DAY_OF_MONTH_TO = 27;

    public static final int HOUR = 8;

    public static final int MIN = 00;

    public static final int SEC = 00;

    public static final String DB_NURSE_FIRST_NAME = "Tamara";

    public static final String DB_NURSE_LAST_NAME = "Simic";

    public static final Long DB_AWAITING_TIME_OFF = 2L;

    public static final Long DB_APPROVED_TIME_OFF = 1L;

    public static final int SERVICE_AWAITING_COUNT = 2;

    public static final int DB_SERVICE_AWAITING_COUNT = 1;
}
