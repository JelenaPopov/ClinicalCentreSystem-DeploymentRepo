package ftn.tim16.ClinicalCentreSystem.dto.requestandresponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import ftn.tim16.ClinicalCentreSystem.model.Room;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class RoomDTO {

    @NotNull(message = "Id is null.")
    private Long id;

    @NotEmpty(message = "Label is empty.")
    @Size(message = "Max size for label is 30.", max = 30)
    private String label;

    @NotNull(message = "Kind is null.")
    private String kind;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime available;


    public RoomDTO(Long id, String label, String kind) {
        this.id = id;
        this.label = label;
        this.kind = kind;
    }

    public RoomDTO(Long id, String label, String kind, LocalDateTime available) {
        this.available = available;
        this.id = id;
        this.label = label;
        this.kind = kind;
    }

    public RoomDTO(Room room) {
        this(room.getId(), room.getLabel(), room.getKind().toString());
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

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public LocalDateTime getAvailable() {
        return available;
    }

    public void setAvailable(LocalDateTime available) {
        this.available = available;
    }
}
