package com.upgrade.volcano.campsite.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.upgrade.volcano.campsite.utils.CustomCampsiteDTOSerializer;

import java.time.LocalDateTime;

public class BookingDTO {
    private Long id;

    private String fullName;

    private String email;

    @JsonSerialize(using = CustomCampsiteDTOSerializer.class)
    private CampsiteDTO campsite;

    private LocalDateTime checkInDateTime;

    private LocalDateTime checkoutDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CampsiteDTO getCampsite() {
        return campsite;
    }

    public void setCampsite(CampsiteDTO campsite) {
        this.campsite = campsite;
    }

    public LocalDateTime getCheckInDateTime() {
        return checkInDateTime;
    }

    public void setCheckInDateTime(LocalDateTime checkInDateTime) {
        this.checkInDateTime = checkInDateTime;
    }

    public LocalDateTime getCheckoutDateTime() {
        return checkoutDateTime;
    }

    public void setCheckoutDateTime(LocalDateTime checkoutDateTime) {
        this.checkoutDateTime = checkoutDateTime;
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", campsite=" + campsite +
                ", checkInDateTime=" + checkInDateTime +
                ", checkoutDateTime=" + checkoutDateTime +
                '}';
    }
}
