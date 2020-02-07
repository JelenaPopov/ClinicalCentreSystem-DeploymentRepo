package ftn.tim16.ClinicalCentreSystem.dto.requestandresponse;

import ftn.tim16.ClinicalCentreSystem.model.Medicine;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class MedicineDTO {

    private Long id;

    @NotEmpty(message = "Label is empty.")
    @Size(message = "Max size for label is 30.", max = 30)
    private String label;

    @NotEmpty(message = "ChemicalComposition is empty.")
    private String chemicalComposition;

    @NotEmpty(message = "Usage is empty.")
    private String usage;

    public MedicineDTO() {

    }

    public MedicineDTO(Long id, String label, String chemicalComposition, String usage) {
        this.id = id;
        this.label = label;
        this.chemicalComposition = chemicalComposition;
        this.usage = usage;
    }

    public MedicineDTO(Medicine medicine) {
        this(medicine.getId(), medicine.getLabel(), medicine.getChemicalComposition(), medicine.getUsage());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getChemicalComposition() {
        return chemicalComposition;
    }

    public void setChemicalComposition(String chemicalComposition) {
        this.chemicalComposition = chemicalComposition;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
