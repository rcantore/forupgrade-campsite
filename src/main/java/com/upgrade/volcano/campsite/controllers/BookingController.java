package com.upgrade.volcano.campsite.controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.upgrade.volcano.campsite.dtos.BookingDTO;
import com.upgrade.volcano.campsite.entities.Booking;
import com.upgrade.volcano.campsite.entities.ResultVO;
import com.upgrade.volcano.campsite.entities.specifications.BookingBetweenDates;
import com.upgrade.volcano.campsite.entities.specifications.BookingForCampsiteId;
import com.upgrade.volcano.campsite.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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
    public ResponseEntity<List<BookingDTO>> getAllBookingsForCampsite(@PathVariable(name = "id") Long campsiteId) {
        //TODO add filtering
        List<BookingDTO> bookingList = bookingService.getAllBookingsForCampsiteId(campsiteId);

        if (bookingList == null || bookingList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(bookingList);
    }

    @PostMapping(path = "/campsites/{id}/bookings")
    public @ResponseBody ResponseEntity createNewBookingForCampsite(@PathVariable(name = "id") Long campsiteId, @RequestBody BookingDTO booking ) {

        ResponseEntity responseEntity;
        try {
            ResultVO resultVO = bookingService.createBookingForCampsiteId(booking, campsiteId);
            if (resultVO.getErrors().isEmpty()) {
                BookingDTO bookingDTO = (BookingDTO) resultVO.getData().get("return");
                responseEntity = ResponseEntity.ok(bookingDTO.getId());
            } else {
                responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultVO.getErrors());
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


        return responseEntity;
    }

    @PutMapping(path = "/campsites/{campsiteId}/bookings/{bookingId}")
    public @ResponseBody ResponseEntity updateBookingForCampsite(
            @PathVariable(name = "campsiteId") Long campsiteId,
            @PathVariable(name = "bookingId") Long bookingId,
            @RequestBody BookingDTO booking ) {

        booking.setId(bookingId);
        bookingService.updateBookingForCampsiteId(booking, campsiteId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/campsites/{campsiteId}/bookings/{bookingId}")
    public @ResponseBody ResponseEntity deleteBookinfForCampsite(
            @PathVariable(name = "campsiteId") Long campsiteId,
            @PathVariable(name = "bookingId") Long bookingId) {

        bookingService.deleteBookingForCampsiteId(bookingId, campsiteId);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(InvalidFormatException.class)
    public final ResponseEntity<String> handleException(InvalidFormatException ex) {
        return new ResponseEntity<>("Invalid format, please check your message", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
