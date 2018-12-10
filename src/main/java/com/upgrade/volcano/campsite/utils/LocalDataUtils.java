package com.upgrade.volcano.campsite.utils;

import com.upgrade.volcano.campsite.entities.Campsite;
import com.upgrade.volcano.campsite.entities.repositories.CampsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocalDataUtils {

    @Autowired
    CampsiteRepository campsiteRepository;

    public void initLocalData() {
        initDefaultCampsiteData();
    }

    private void initDefaultCampsiteData() {
        Campsite campsite = new Campsite();
        campsite.setId(1L);
        campsite.setName("New Volcano Island");
        campsite.setLocation("Pacific Ocean");

        campsiteRepository.save(campsite);

    }
}
