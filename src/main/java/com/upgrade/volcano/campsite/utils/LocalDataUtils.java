package com.upgrade.volcano.campsite.utils;

import com.upgrade.volcano.campsite.entities.Booking;
import com.upgrade.volcano.campsite.entities.Campsite;
import com.upgrade.volcano.campsite.entities.repositories.BookingRepository;
import com.upgrade.volcano.campsite.entities.repositories.CampsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class LocalDataUtils {

    @Autowired
    CampsiteRepository campsiteRepository;

    @Autowired
    BookingRepository bookingRepository;

    public void initLocalData() {
        initDefaultCampsiteData();
        initDefaultBookingData();
    }

    private void initDefaultCampsiteData() {
        Campsite campsite = new Campsite();
        campsite.setId(1L);
        campsite.setName("New Volcano Island");
        campsite.setLocation("Pacific Ocean");

        campsiteRepository.save(campsite);

    }

    private void initDefaultBookingData() {
        Booking booking1 = new Booking();

        LocalDateTime checkInTime = LocalDateTime.of(LocalDate.now().plusDays(5), LocalTime.of(12,00));
        booking1.setCheckInDateTime(checkInTime);

        LocalDateTime checkOutTime = LocalDateTime.of(LocalDate.now().plusDays(8), LocalTime.of(12,00));
        booking1.setCheckoutDateTime(checkOutTime);

        booking1.setEmail("somemail@somedomain.com");
        booking1.setFullName("Edgard Camper");

        bookingRepository.save(booking1);

        Booking booking2 = new Booking();

        checkInTime = LocalDateTime.of(LocalDate.now().plusDays(25), LocalTime.of(12,00));
        booking2.setCheckInDateTime(checkInTime);

        checkOutTime = LocalDateTime.of(LocalDate.now().plusDays(26), LocalTime.of(12,00));
        booking2.setCheckoutDateTime(checkOutTime);

        booking2.setEmail("jdove@anotherdomain.com");
        booking2.setFullName("Jane Dove");

        bookingRepository.save(booking2);

        campsiteRepository.findById(1L).ifPresent(campsite -> {
            campsite.getBookings().add(booking1);
            campsite.getBookings().add(booking2);
            campsiteRepository.save(campsite);

            booking1.setCampsite(campsite);
            bookingRepository.save(booking1);

            booking2.setCampsite(campsite);
            bookingRepository.save(booking2);

        });


    }
}
