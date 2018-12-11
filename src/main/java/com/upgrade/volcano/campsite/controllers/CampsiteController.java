package com.upgrade.volcano.campsite.controllers;

import com.upgrade.volcano.campsite.dtos.CampsiteDTO;
import com.upgrade.volcano.campsite.services.BookingService;
import com.upgrade.volcano.campsite.services.CampsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<CampsiteDTO>> getAvailabilityForCampsite(@PathVariable("id") Long campsiteId) {
        //TODO add filtering
        List<CampsiteDTO> campsiteList = campsiteService.getAllCampsite();

        if (campsiteList == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(campsiteList);
    }

}
