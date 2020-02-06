package ftn.tim16.ClinicalCentreSystem.dto.request;

import javax.validation.constraints.NotEmpty;

public class CreateTimeOffRequestDTO {
    @NotEmpty(message = "Type is empty.")
    private String type;

    @NotEmpty(message = "Start date and time is empty.")
    private String startDateTime;

    @NotEmpty(message = "End date and time is empty.")
    private String endDateTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }
}
