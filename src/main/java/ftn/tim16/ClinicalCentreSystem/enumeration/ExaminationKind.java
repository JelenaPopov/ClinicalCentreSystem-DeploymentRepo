package ftn.tim16.ClinicalCentreSystem.enumeration;

public enum ExaminationKind {
    EXAMINATION {
        @Override
        public String toString() {
            return "EXAMINATION";
        }
    },
    OPERATION {
        @Override
        public String toString() {
            return "OPERATION";
        }
    }
}
