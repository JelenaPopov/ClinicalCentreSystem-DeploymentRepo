package ftn.tim16.ClinicalCentreSystem.dto.request;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.DoctorDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ExaminationTypeDTO;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PredefinedExaminationDTO {

    @NotEmpty(message = "Start date and time is empty.")
    private String startDateTime;

    @NotEmpty(message = "End date and time is empty.")
    private String endDateTime;

    @NotNull(message = "Examination type is null")
    private ExaminationTypeDTO examinationTypeDTO;


    @NotNull(message = "Doctor  is null")
    private DoctorDTO doctorDTO;

    @NotNull(message = "Room  is null")
    private Long room;

    @NotNull(message = "Discount  is null")
    @Range(min = 1, max = 99)
    @Positive(message = "Discount must be number grater than 0.")
    private Integer discount;

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

    public ExaminationTypeDTO getExaminationTypeDTO() {
        return examinationTypeDTO;
    }

    public void setExaminationTypeDTO(ExaminationTypeDTO examinationTypeDTO) {
        this.examinationTypeDTO = examinationTypeDTO;
    }

    public DoctorDTO getDoctorDTO() {
        return doctorDTO;
    }

    public void setDoctorDTO(DoctorDTO doctorDTO) {
        this.doctorDTO = doctorDTO;
    }

    public Long getRoom() {
        return room;
    }

    public void setRoom(Long room) {
        this.room = room;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}
