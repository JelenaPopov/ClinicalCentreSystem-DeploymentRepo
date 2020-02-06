package ftn.tim16.ClinicalCentreSystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ftn.tim16.ClinicalCentreSystem.enumeration.DoctorStatus;
import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Doctor implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Email is empty.")
    @Email(message = "Email is invalid.")
    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @NotEmpty(message = "First name is empty.")
    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    private String firstName;

    @NotEmpty(message = "Last name is empty.")
    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    private String lastName;

    @NotEmpty(message = "Phone number is empty.")
    @Size(min = 9, max = 10)
    @Pattern(regexp = "0[0-9]+")
    @Column(columnDefinition = "VARCHAR(11)", unique = true, nullable = false)
    private String phoneNumber;

    @JsonFormat(pattern = "HH:mm")
    @NotNull
    @Column(nullable = false)
    private LocalTime workHoursFrom;

    @JsonFormat(pattern = "HH:mm")
    @NotNull
    @Column(nullable = false)
    private LocalTime workHoursTo;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Clinic clinic;

    @ManyToMany(mappedBy = "doctors", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Examination> examinations = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ExaminationType specialized;

    @JsonManagedReference
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TimeOffDoctor> timeOffDoctors = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private DoctorStatus status;

    @Column
    private Timestamp lastPasswordResetDate = new Timestamp(DateTime.now().getMillis());

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "doctor_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private Set<Authority> authorities;

    @Column
    private Double doctorRating;

    public Doctor() {

    }

    public Doctor(String email, String password, String firstName, String lastName, String phoneNumber, LocalTime workHoursFrom,
                  LocalTime workHoursTo, Clinic clinic, ExaminationType specialized, DoctorStatus status, Set<Authority> authorities) {
        this.email = email;
        setPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.workHoursFrom = workHoursFrom;
        this.workHoursTo = workHoursTo;
        this.clinic = clinic;
        this.specialized = specialized;
        this.status = status;
        this.timeOffDoctors = new HashSet<>();
        this.examinations = new HashSet<Examination>();
        this.authorities = authorities;
        this.doctorRating = 0.0;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (status == DoctorStatus.ACTIVE);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        Timestamp now = new Timestamp(DateTime.now().getMillis());
        this.setLastPasswordResetDate(now);
        this.password = password;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalTime getWorkHoursFrom() {
        return workHoursFrom;
    }

    public void setWorkHoursFrom(LocalTime workHoursFrom) {
        this.workHoursFrom = workHoursFrom;
    }

    public LocalTime getWorkHoursTo() {
        return workHoursTo;
    }

    public void setWorkHoursTo(LocalTime workHoursTo) {
        this.workHoursTo = workHoursTo;
    }

    public DoctorStatus getStatus() {
        return status;
    }

    public void setStatus(DoctorStatus status) {
        this.status = status;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public Set<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(Set<Examination> examinations) {
        this.examinations = examinations;
    }

    public ExaminationType getSpecialized() {
        return specialized;
    }

    public void setSpecialized(ExaminationType specialized) {
        this.specialized = specialized;
    }

    public Set<TimeOffDoctor> getTimeOffDoctors() {
        return timeOffDoctors;
    }

    public void setTimeOffDoctors(Set<TimeOffDoctor> timeOffDoctors) {
        this.timeOffDoctors = timeOffDoctors;
    }

    public Timestamp getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public Double getDoctorRating() {
        return doctorRating;
    }

    public void setDoctorRating(Double doctorRating) {
        this.doctorRating = doctorRating;
    }

    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public boolean isAvailable(LocalTime startExaminationTime, LocalTime endExaminationTime) {
        if ((startExaminationTime.isAfter(workHoursFrom) || startExaminationTime.equals(workHoursFrom)) && startExaminationTime.isBefore(workHoursTo)) {
            if (endExaminationTime.isAfter(workHoursFrom) && (endExaminationTime.isBefore(workHoursTo) || endExaminationTime.equals(workHoursTo))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Doctor doctor = (Doctor) o;
        if (doctor.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, doctor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
