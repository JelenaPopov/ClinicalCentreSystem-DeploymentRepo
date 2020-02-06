package ftn.tim16.ClinicalCentreSystem.dto.response;

import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffStatus;
import ftn.tim16.ClinicalCentreSystem.model.DateTimeInterval;
import ftn.tim16.ClinicalCentreSystem.model.TimeOffDoctor;
import ftn.tim16.ClinicalCentreSystem.model.TimeOffNurse;

public class RequestForTimeOffDTO {
    private Long id;

    private String type;

    private DateTimeInterval interval;

    private String firstName;

    private String lastName;

    private TimeOffStatus status;

    public RequestForTimeOffDTO() {

    }

    public RequestForTimeOffDTO(Long id, String type, DateTimeInterval interval,
                                String firstName, String lastName, TimeOffStatus timeOffStatus) {
        this.id = id;
        this.type = type;
        this.interval = interval;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = timeOffStatus;
    }

    public RequestForTimeOffDTO(TimeOffNurse timeOffNurse) {
        this(timeOffNurse.getId(), timeOffNurse.getType().toString(), timeOffNurse.getInterval(),
                timeOffNurse.getNurse().getFirstName(), timeOffNurse.getNurse().getLastName(), timeOffNurse.getStatus());
    }

    public RequestForTimeOffDTO(TimeOffDoctor timeOffDoctor) {
        this(timeOffDoctor.getId(), timeOffDoctor.getType().toString(), timeOffDoctor.getInterval(),
                timeOffDoctor.getDoctor().getFirstName(), timeOffDoctor.getDoctor().getLastName(), timeOffDoctor.getStatus());
    }

    public TimeOffStatus getStatus() {
        return status;
    }

    public void setStatus(TimeOffStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DateTimeInterval getInterval() {
        return interval;
    }

    public void setInterval(DateTimeInterval interval) {
        this.interval = interval;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
