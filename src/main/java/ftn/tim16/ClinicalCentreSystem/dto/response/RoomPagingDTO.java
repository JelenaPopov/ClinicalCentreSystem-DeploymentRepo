package ftn.tim16.ClinicalCentreSystem.dto.response;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.RoomDTO;

import java.util.List;

public class RoomPagingDTO {

    private List<RoomDTO> roomDTOList;
    private Integer numberOfItems;

    public RoomPagingDTO(List<RoomDTO> roomDTOList, Integer numberOfItems) {
        this.roomDTOList = roomDTOList;
        this.numberOfItems = numberOfItems;
    }

    public List<RoomDTO> getRoomDTOList() {
        return roomDTOList;
    }

    public void setRoomDTOList(List<RoomDTO> roomDTOList) {
        this.roomDTOList = roomDTOList;
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(Integer numberOfItems) {
        this.numberOfItems = numberOfItems;
    }
}
