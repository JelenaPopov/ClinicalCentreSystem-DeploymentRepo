package ftn.tim16.ClinicalCentreSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(50)", nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "VARCHAR")
    private String description;

    @Column(nullable = false)
    private String address;

    @Column()
    private Double clinicRating;

    //You can't change this fetch type
    @JsonIgnore
    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Examination> examinations = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Doctor> doctors = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Nurse> nurses = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Room> rooms = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ExaminationType> examinationTypes = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ClinicAdministrator> clinicAdministrators = new HashSet<>();

    @Version
    private Long version;

    public Clinic() {
    }

    public Clinic(String name, String description, String address) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.examinations = new HashSet<>();
        this.doctors = new HashSet<>();
        this.nurses = new HashSet<>();
        this.rooms = new HashSet<>();
        this.examinationTypes = new HashSet<>();
        this.clinicAdministrators = new HashSet<>();
        this.clinicRating = 0.0;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public Set<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(Set<Examination> examinations) {
        this.examinations = examinations;
    }

    public Set<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }

    public Set<Nurse> getNurses() {
        return nurses;
    }

    public void setNurses(Set<Nurse> nurses) {
        this.nurses = nurses;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    public Set<ExaminationType> getExaminationTypes() {
        return examinationTypes;
    }

    public void setExaminationTypes(Set<ExaminationType> examinationTypes) {
        this.examinationTypes = examinationTypes;
    }

    public Set<ClinicAdministrator> getClinicAdministrators() {
        return clinicAdministrators;
    }

    public void setClinicAdministrators(Set<ClinicAdministrator> clinicAdministrators) {
        this.clinicAdministrators = clinicAdministrators;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getClinicRating() {
        return clinicRating;
    }

    public void setClinicRating(Double clinicRating) {
        this.clinicRating = clinicRating;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Clinic clinic = (Clinic) o;
        if (clinic.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, clinic.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
