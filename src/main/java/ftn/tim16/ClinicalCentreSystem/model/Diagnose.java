package ftn.tim16.ClinicalCentreSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Diagnose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, columnDefinition = "VARCHAR(30)", nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "VARCHAR")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "diagnose", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ExaminationReport> examinationReports = new HashSet<>();

    public Diagnose() {

    }

    public Diagnose(String title, String description) {
        this.title = title;
        this.description = description;
        this.examinationReports = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ExaminationReport> getExaminationReports() {
        return examinationReports;
    }

    public void setExaminationReports(Set<ExaminationReport> examinationReports) {
        this.examinationReports = examinationReports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Diagnose diagnose = (Diagnose) o;
        if (diagnose.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, diagnose.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
