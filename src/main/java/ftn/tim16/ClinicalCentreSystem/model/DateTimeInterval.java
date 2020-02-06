package ftn.tim16.ClinicalCentreSystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class DateTimeInterval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @OneToMany(mappedBy = "interval", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TimeOffDoctor> timeOffDoctors = new HashSet<>();

    @OneToMany(mappedBy = "interval", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Examination> examinations = new HashSet<>();

    public DateTimeInterval() {
    }

    public DateTimeInterval(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        timeOffDoctors = new HashSet<>();
        examinations = new HashSet<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public boolean isAvailable(LocalDateTime startExaminationTime, LocalDateTime endExaminationTime) {
        if (startExaminationTime.isEqual(startDateTime) || endExaminationTime.isEqual(endDateTime)) {
            return false;
        }

        if (startExaminationTime.isAfter(startDateTime) && startExaminationTime.isBefore(endDateTime)) {
            return false;
        }

        if (endExaminationTime.isAfter(startDateTime) && endExaminationTime.isBefore(endDateTime)) {
            return false;
        }

      /*  if (startExaminationTime.isBefore(startDateTime) && endExaminationTime.isAfter(startDateTime)) {
            return false;
        }*/
        return !(startExaminationTime.isBefore(startDateTime) && endExaminationTime.isAfter(endDateTime));
    }
}
