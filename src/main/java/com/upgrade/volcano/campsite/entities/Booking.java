package com.upgrade.volcano.campsite.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import java.time.LocalDateTime;

@Entity
public class Booking extends AbstractEntity {

    @ManyToOne
    private Campsite campsite;

    private String fullName;

    private String email;

    private LocalDateTime checkInDateTime;

    private LocalDateTime checkoutDateTime;

    public Campsite getCampsite() {
        return campsite;
    }

    public void setCampsite(Campsite campsite) {
        this.campsite = campsite;
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


}
