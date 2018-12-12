package com.upgrade.volcano.campsite.controllers;

import com.upgrade.volcano.campsite.dtos.CampingDateDTO;
import com.upgrade.volcano.campsite.dtos.CampsiteDTO;
import com.upgrade.volcano.campsite.services.BookingService;
import com.upgrade.volcano.campsite.services.CampsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CampsiteController {
    @Autowired
    CampsiteService campsiteService;

    @Autowired
    BookingService bookingService;

    @GetMapping(path = "/campsites")
    public ResponseEntity<List<CampsiteDTO>> getAllCampsites() {
        //TODO add filtering
        List<CampsiteDTO> campsiteList = campsiteService.getAllCampsite();

        if (campsiteList == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(campsiteList);
    }

    @GetMapping(path = "/campsites/{id}/availability")
    public ResponseEntity<List<CampingDateDTO>> getAvailabilityForCampsite(@PathVariable("id") Long campsiteId,
                                                                        @RequestParam(value = "checkInDate", required = false)
                                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                                LocalDate checkInDate,
                                                                        @RequestParam(value = "checkoutDate", required = false)
                                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                                LocalDate checkoutDate) {
        List<CampingDateDTO> campsiteList = campsiteService.getAvailabilityForCampsiteIdBetweenDates(campsiteId, checkInDate, checkoutDate);

        if (campsiteList == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(campsiteList);
    }

}
