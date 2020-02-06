package ftn.tim16.ClinicalCentreSystem.constants;

import ftn.tim16.ClinicalCentreSystem.enumeration.PatientStatus;

public class PatientConstants {
    public static final Long PATIENT_ID = 1L;
    public static final String PATIENT_EMAIL = "Patient1@maildrop.cc";
    public static final String PATIENT_PASSWORD = "$2a$10$ItcRjGVnH26jIqw7GRXOBeM5Wu3sGxkSdvSgTRWSjEpPno4J9T4kS";
    public static final String PATIENT_FIRST_NAME = "Milovan";
    public static final String PATIENT_LAST_NAME = "Milic";
    public static final String PATIENT_PHONE_NUMBER = "065258255";
    public static final String PATIENT_ADDRESS = "Marsala tita 13";
    public static final String PATIENT_CITY = "Novi Sad";
    public static final String PATIENT_COUNTRY = "Srbija";
    public static final String PATIENT_HEALTH_INSURANCE_ID = "0625351236915";
    public static final PatientStatus PATIENT_STATUS_AWAITING_APPROVAL = PatientStatus.AWAITING_APPROVAL;
    public static final String PATIENT_ROLE = "ROLE_PATIENT";

    public static final Long NEW_PATIENT_ID = 4L;
    public static final String NEW_PATIENT_EMAIL = "Patient4@maildrop.cc";
    public static final String NEW_PATIENT_PASSWORD = "$2a$10$ItcRjGVnH26jIqw7GRXOBeM5Wu3sGxkSdvSgTRWSjEpPno4J9T4kS";
    public static final String NEW_PATIENT_FIRST_NAME = "Pavle";
    public static final String NEW_PATIENT_LAST_NAME = "Milivojevic";
    public static final String NEW_PATIENT_PHONE_NUMBER = "065568255";
    public static final String NEW_PATIENT_ADDRESS = "Vojvodjanska 17";
    public static final String NEW_PATIENT_CITY = "Zrenjanin";
    public static final String NEW_PATIENT_COUNTRY = "Srbija";
    public static final String NEW_PATIENT_HEALTH_INSURANCE_ID = "0725351286815";
    public static final PatientStatus NEW_PATIENT_STATUS_AWAITING_APPROVAL = PatientStatus.AWAITING_APPROVAL;

    public static final Long ACTIVATED_PATIENT_ID = 3L;
    public static final String REASON_FOR_REJECTION = "Some reason";

    public static final int DB_COUNT = 3;
    public static final int DB_AWAITING_APPROVAL_COUNT = 2;
}
