package com.upgrade.volcano.campsite.controllers;

import com.upgrade.volcano.campsite.dtos.BookingDTO;
import com.upgrade.volcano.campsite.entities.Booking;
import com.upgrade.volcano.campsite.entities.specifications.BookingBetweenDates;
import com.upgrade.volcano.campsite.entities.specifications.BookingForCampsiteId;
import com.upgrade.volcano.campsite.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class BookingController {

    @Autowired
    BookingService bookingService;

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDTO>> getAllBookings(@RequestParam(value = "campsiteId", required = false) Long campsiteId,
                                                           @RequestParam(value = "checkInDateTime", required = false)
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                   LocalDateTime checkInDateTime,
                                                           @RequestParam(value = "checkoutDateTime", required = false)
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                       LocalDateTime checkoutDateTime)  {
        Specification<Booking> spec = Specification.where(new BookingForCampsiteId(campsiteId))
                .and(new BookingBetweenDates(checkInDateTime, checkoutDateTime));

        List<BookingDTO> bookingList = bookingService.getAllBookings(spec);

        if (bookingList == null || bookingList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(bookingList);
    }

    @GetMapping(path = "/campsites/{id}/bookings")
    public ResponseEntity<List<BookingDTO>> getAllBookingsForCamspite(@PathVariable(name = "id") Long campsiteId) {
        //TODO add filtering
        List<BookingDTO> bookingList = bookingService.getAllBookingsForCampsiteId(campsiteId);

        if (bookingList == null || bookingList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(bookingList);
    }
}
