package com.upgrade.volcano.campsite.services;

import com.upgrade.volcano.campsite.dtos.CampsiteDTO;
import com.upgrade.volcano.campsite.entities.Campsite;
import com.upgrade.volcano.campsite.entities.repositories.CampsiteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CampsiteService {

    @Autowired
    CampsiteRepository campsiteRepository;

    public List<CampsiteDTO> getAllCampsite() {
        ModelMapper modelMapper = new ModelMapper();

        List<CampsiteDTO> campsiteDTOS = new ArrayList<>();
        List<Campsite> campsiteList = campsiteRepository.findAll();
        campsiteList.forEach(campsite -> campsiteDTOS.add(modelMapper.map(campsite, CampsiteDTO.class)));

        return campsiteDTOS;
    }


    public List<CampsiteDTO> getAvailabilityForCampsiteId(Long campsiteId) {
        Optional<Campsite> optionalCampsite = campsiteRepository.findById(campsiteId);

        ModelMapper modelMapper = new ModelMapper();

        List<CampsiteDTO> campsiteDTOS = new ArrayList<>();
        List<Campsite> campsiteList = campsiteRepository.findAll();
        campsiteList.forEach(campsite -> campsiteDTOS.add(modelMapper.map(campsite, CampsiteDTO.class)));

        return campsiteDTOS;
    }

}
