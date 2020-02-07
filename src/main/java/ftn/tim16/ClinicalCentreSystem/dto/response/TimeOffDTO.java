package ftn.tim16.ClinicalCentreSystem.dto.response;

import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffType;
import ftn.tim16.ClinicalCentreSystem.model.DateTimeInterval;
import ftn.tim16.ClinicalCentreSystem.model.TimeOffDoctor;
import ftn.tim16.ClinicalCentreSystem.model.TimeOffNurse;

public class TimeOffDTO {
    private Long id;

    private TimeOffType type;

    private DateTimeInterval interval;

    public TimeOffDTO() {

    }

    public TimeOffDTO(Long id, TimeOffType type, DateTimeInterval interval) {
        this.id = id;
        this.type = type;
        this.interval = interval;
    }

    public TimeOffDTO(TimeOffNurse timeOffNurse) {
        this(timeOffNurse.getId(), timeOffNurse.getType(), timeOffNurse.getInterval());
    }

    public TimeOffDTO(TimeOffDoctor timeOffDoctor) {
        this(timeOffDoctor.getId(), timeOffDoctor.getType(), timeOffDoctor.getInterval());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TimeOffType getType() {
        return type;
    }

    public void setType(TimeOffType type) {
        this.type = type;
    }

    public DateTimeInterval getInterval() {
        return interval;
    }

    public void setInterval(DateTimeInterval interval) {
        this.interval = interval;
    }
}
