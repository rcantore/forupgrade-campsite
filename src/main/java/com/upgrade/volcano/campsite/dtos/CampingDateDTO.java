package com.upgrade.volcano.campsite.dtos;

import java.time.LocalDateTime;

public class CampingDateDTO {

    private LocalDateTime checkInDateTime;

    private LocalDateTime checkoutDateTime;

    private BookingDTO booking;

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

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    public Boolean isAvailable() {
        return this.booking == null;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
