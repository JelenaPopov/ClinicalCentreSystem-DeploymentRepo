package ftn.tim16.ClinicalCentreSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffType;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class TimeOffDoctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TimeOffType type;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private DateTimeInterval interval;

    @Enumerated(EnumType.STRING)
    private TimeOffStatus status;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Doctor doctor;

    @Version
    private Long version;

    public TimeOffDoctor() {
    }

    public TimeOffDoctor(TimeOffType type, DateTimeInterval interval, TimeOffStatus status, Doctor doctor) {
        this.type = type;
        this.interval = interval;
        this.status = status;
        this.doctor = doctor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
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

    public TimeOffStatus getStatus() {
        return status;
    }

    public void setStatus(TimeOffStatus status) {
        this.status = status;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeOffDoctor timeOffDoctor = (TimeOffDoctor) o;
        if (timeOffDoctor.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, timeOffDoctor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
