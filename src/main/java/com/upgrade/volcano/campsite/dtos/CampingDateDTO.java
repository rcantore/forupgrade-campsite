package com.upgrade.volcano.campsite.dtos;

import java.time.LocalDateTime;

public class CampingDateDTO {

    private LocalDateTime checkInDateTime;

    private LocalDateTime checkoutDateTime;

    private Long bookingId;

    private Boolean available;

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

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Boolean isAvailable() {
        return this.bookingId == null;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
