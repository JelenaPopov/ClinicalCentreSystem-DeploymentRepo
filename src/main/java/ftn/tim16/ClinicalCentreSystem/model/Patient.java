package ftn.tim16.ClinicalCentreSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ftn.tim16.ClinicalCentreSystem.enumeration.PatientStatus;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Component("patient")
@Scope("prototype")
public class Patient implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    private String firstName;

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    private String lastName;

    @Column(columnDefinition = "VARCHAR(10)", unique = true, nullable = false)
    private String phoneNumber;

    @Column(columnDefinition = "VARCHAR(50)", nullable = false)
    private String address;

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    private String city;

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    private String country;

    @Column(name = "healthInsuranceId", columnDefinition = "VARCHAR(13)", unique = true, nullable = false)
    private String healthInsuranceId;

    @Enumerated(EnumType.STRING)
    private PatientStatus status;

    @OneToOne(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MedicalRecord medicalRecord;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Examination> examinations = new HashSet<>();

    @Column
    private Timestamp lastPasswordResetDate = new Timestamp(DateTime.now().getMillis());

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "patient_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private Set<Authority> authorities;

    @Version
    private Long version;

    public Patient() {

    }

    public Patient(String email, String password, String firstName, String lastName, String phoneNumber, String address,
                   String city, String country, String healthInsuranceID, Set<Authority> authorities) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.country = country;
        this.healthInsuranceId = healthInsuranceID;
        this.status = PatientStatus.AWAITING_APPROVAL;
        this.medicalRecord = null;
        this.examinations = new HashSet<>();
        Timestamp now = new Timestamp(DateTime.now().getMillis());
        this.lastPasswordResetDate = now;
        this.authorities = authorities;
    }

    public Patient(Patient patient) {
        this.id = patient.getId();
        this.email = patient.getEmail();
        this.password = patient.getPassword();
        this.firstName = patient.getFirstName();
        this.lastName = patient.getLastName();
        this.phoneNumber = patient.getPhoneNumber();
        this.address = patient.getAddress();
        this.city = patient.getCity();
        this.country = patient.getCountry();
        this.healthInsuranceId = patient.getHealthInsuranceId();
        this.status = patient.getStatus();
        this.medicalRecord = patient.getMedicalRecord();
        this.examinations = patient.getExaminations();
        this.lastPasswordResetDate = patient.getLastPasswordResetDate();
        this.authorities = patient.authorities;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
        return (status == PatientStatus.ACTIVATED);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHealthInsuranceId() {
        return healthInsuranceId;
    }

    public void setHealthInsuranceId(String healthInsuranceId) {
        this.healthInsuranceId = healthInsuranceId;
    }

    public PatientStatus getStatus() {
        return status;
    }

    public void setStatus(PatientStatus status) {
        this.status = status;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public Set<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(Set<Examination> examinations) {
        this.examinations = examinations;
    }

    public Timestamp getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Patient patient = (Patient) o;
        if (patient.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String getPrototypeBeanName() {
        return "patientPrototype";
    }

    @Bean("patientPrototype")
    @Scope("prototype")
    public static Patient clone(Patient patient) {
        return new Patient(patient);
    }
}
