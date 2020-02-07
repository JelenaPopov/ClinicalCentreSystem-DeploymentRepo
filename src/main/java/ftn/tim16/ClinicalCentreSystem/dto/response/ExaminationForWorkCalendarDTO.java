package ftn.tim16.ClinicalCentreSystem.dto.response;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.PatientWithIdDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.RoomWithIdDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationStatus;
import ftn.tim16.ClinicalCentreSystem.model.DateTimeInterval;
import ftn.tim16.ClinicalCentreSystem.model.Examination;

public class ExaminationForWorkCalendarDTO {
    private Long id;
    private String kind;
    private DateTimeInterval interval;
    private RoomWithIdDTO room;
    private PatientWithIdDTO patient;

    public ExaminationForWorkCalendarDTO(Examination examination) {
        this.id = examination.getId();
        this.interval = examination.getInterval();
        if (examination.getRoom() != null) {
            this.room = new RoomWithIdDTO(examination.getRoom());
        } else {
            this.room = null;
        }
        if (examination.getPatient() != null) {
            this.patient = new PatientWithIdDTO(examination.getPatient());
        } else {
            this.patient = null;
        }

        this.kind = examination.getKind().toString();
        if (examination.getStatus() == ExaminationStatus.PREDEF_BOOKED || examination.getStatus() == ExaminationStatus.PREDEF_AVAILABLE) {
            this.kind = "PREDEF. EXAMINATION";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public DateTimeInterval getInterval() {
        return interval;
    }

    public void setInterval(DateTimeInterval interval) {
        this.interval = interval;
    }

    public RoomWithIdDTO getRoom() {
        return room;
    }

    public void setRoom(RoomWithIdDTO room) {
        this.room = room;
    }

    public PatientWithIdDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientWithIdDTO patient) {
        this.patient = patient;
    }
}
