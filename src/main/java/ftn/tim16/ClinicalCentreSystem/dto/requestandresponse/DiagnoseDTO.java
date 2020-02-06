package ftn.tim16.ClinicalCentreSystem.dto.requestandresponse;

import ftn.tim16.ClinicalCentreSystem.model.Diagnose;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class DiagnoseDTO {

    private Long id;

    @NotEmpty(message = "Title is empty.")
    @Size(message = "Max size for label is 30.", max = 30)
    private String title;

    @NotEmpty(message = "Description is empty.")
    private String description;

    public DiagnoseDTO() {

    }

    public DiagnoseDTO(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public DiagnoseDTO(Diagnose diagnose) {
        this(diagnose.getId(), diagnose.getTitle(), diagnose.getDescription());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
