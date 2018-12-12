package com.upgrade.volcano.campsite.dtos;

import java.util.List;

public class AvailabilityDTO {

    private Long campsiteId;

    private List<CampingDateDTO> dates;

    public Long getCampsiteId() {
        return campsiteId;
    }

    public void setCampsiteId(Long campsiteId) {
        this.campsiteId = campsiteId;
    }

    public List<CampingDateDTO> getDates() {
        return dates;
    }

    public void setDates(List<CampingDateDTO> dates) {
        this.dates = dates;
    }
}
