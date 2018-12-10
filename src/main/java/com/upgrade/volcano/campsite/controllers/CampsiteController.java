package com.upgrade.volcano.campsite.controllers;

import com.upgrade.volcano.campsite.entities.Campsite;
import com.upgrade.volcano.campsite.entities.repositories.CampsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/campsites")
public class CampsiteController {
    @Autowired
    CampsiteRepository campsiteRepository;

    @GetMapping
    public ResponseEntity<List<Campsite>> getAllCampsites() {
        //TODO add filtering
        List<Campsite> campsiteList = campsiteRepository.findAll();

        if (campsiteList == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(campsiteList);
    }
}
