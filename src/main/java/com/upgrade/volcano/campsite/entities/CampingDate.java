package com.upgrade.volcano.campsite.entities;

import java.time.LocalDateTime;

public class CampingDate {
    private Campsite campsite;

    private LocalDateTime checkInDateTime;

    private LocalDateTime checkoutDateTime;

    private Booking booking;

    private Boolean available;

    public Campsite getCampsite() {
        return campsite;
    }

    public void setCampsite(Campsite campsite) {
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

    public Boolean isAvailable() {
        return this.booking == null;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
