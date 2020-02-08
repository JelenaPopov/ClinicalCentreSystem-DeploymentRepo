package ftn.tim16.ClinicalCentreSystem.enumeration;

public enum TimeOffType {
    HOLIDAY {
        @Override
        public String toString() {
            return "holiday";
        }
    },
    TIME_OFF {
        @Override
        public String toString() {
            return "time off";
        }
    },
}
