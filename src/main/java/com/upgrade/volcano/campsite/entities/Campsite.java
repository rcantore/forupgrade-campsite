package com.upgrade.volcano.campsite.entities;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.List;

@Entity
public class Campsite extends AbstractEntity {

    private String name;

    private String location;

    @OneToMany
    private List<Booking> bookings;

    @Transient
    private List<CampingDate> campingDates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
