package com.upgrade.volcano.campsite.services;

import com.upgrade.volcano.campsite.dtos.BookingDTO;
import com.upgrade.volcano.campsite.dtos.CampingDateDTO;
import com.upgrade.volcano.campsite.dtos.CampsiteDTO;
import com.upgrade.volcano.campsite.entities.Booking;
import com.upgrade.volcano.campsite.entities.Campsite;
import com.upgrade.volcano.campsite.entities.repositories.CampsiteRepository;
import com.upgrade.volcano.campsite.entities.specifications.BookingBetweenDates;
import com.upgrade.volcano.campsite.entities.specifications.BookingForCampsiteId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CampsiteService {

    @Autowired
    private CampsiteRepository campsiteRepository;

    @Autowired
    private BookingService bookingService;


    public List<CampsiteDTO> getAllCampsite() {
        ModelMapper modelMapper = new ModelMapper();

        List<CampsiteDTO> campsiteDTOS = new ArrayList<>();
        List<Campsite> campsiteList = campsiteRepository.findAll();
        campsiteList.forEach(campsite -> campsiteDTOS.add(modelMapper.map(campsite, CampsiteDTO.class)));

        return campsiteDTOS;
    }


    public List<CampingDateDTO> getAvailabilityForCampsiteIdBetweenDates(Long campsiteId, LocalDate checkInDate, LocalDate checkoutDate) {
        Optional<Campsite> optionalCampsite = campsiteRepository.findById(campsiteId);

        List<CampingDateDTO> dates = null;
        if (optionalCampsite.isPresent()) {
            Campsite campsite = optionalCampsite.get();

            if (checkInDate == null) {
                checkInDate = LocalDate.now();
            }

            if (checkoutDate == null) {
                checkoutDate = checkInDate.plusDays(30);
            }

            LocalDateTime checkInDateTime = LocalDateTime.of(checkInDate, LocalTime.of(10, 00));
            LocalDateTime checkoutDateTime = LocalDateTime.of(checkoutDate, LocalTime.of(10, 00));

            Specification<Booking> spec = Specification.where(new BookingForCampsiteId(campsiteId))
                    .and(new BookingBetweenDates(checkInDateTime, checkoutDateTime));

            List<BookingDTO> bookingList = bookingService.getAllBookings(spec);

            dates = buildAvailabilityCalendar(checkInDateTime, checkoutDateTime, bookingList);

        }

        ModelMapper modelMapper = new ModelMapper();

        List<CampsiteDTO> campsiteDTOS = new ArrayList<>();
        List<Campsite> campsiteList = campsiteRepository.findAll();
        campsiteList.forEach(campsite -> campsiteDTOS.add(modelMapper.map(campsite, CampsiteDTO.class)));

        return dates;
    }

    private List<CampingDateDTO> buildAvailabilityCalendar(LocalDateTime checkInDateTime, LocalDateTime checkoutDateTime, List<BookingDTO> bookingDTOList) {

        List<CampingDateDTO> campingDateList = new ArrayList<>();

        if (bookingDTOList.isEmpty()) {
            for (int i = 0; i < ChronoUnit.DAYS.between(checkInDateTime, checkoutDateTime); i++) {
                CampingDateDTO campingDateDTO = new CampingDateDTO();

                LocalDateTime day = checkInDateTime.plusDays(i);
                campingDateDTO.setCheckInDateTime(day);

                LocalDateTime nextDay = checkInDateTime.plusDays(i + 1);
                campingDateDTO.setCheckoutDateTime(nextDay);

                campingDateList.add(campingDateDTO);

            }
        } else {
            LocalDateTime pivotLocalDateTime = checkInDateTime;
            for (BookingDTO bookingDTO : bookingDTOList) {
                for (int i = 0; i < ChronoUnit.DAYS.between(pivotLocalDateTime, bookingDTO.getCheckInDateTime()) - 1; i++) {
                    CampingDateDTO campingDateDTO = new CampingDateDTO();

                    LocalDateTime day = pivotLocalDateTime.plusDays(i);
                    campingDateDTO.setCheckInDateTime(day);

                    LocalDateTime nextDay = pivotLocalDateTime.plusDays(i + 1);
                    campingDateDTO.setCheckoutDateTime(nextDay);

                    campingDateList.add(campingDateDTO);

                }

                pivotLocalDateTime = bookingDTO.getCheckInDateTime();
                for (int i = 0; i < ChronoUnit.DAYS.between(bookingDTO.getCheckInDateTime(), bookingDTO.getCheckoutDateTime()); i++) {
                    CampingDateDTO campingDateDTO = new CampingDateDTO();

                    LocalDateTime day = pivotLocalDateTime.plusDays(i);
                    campingDateDTO.setCheckInDateTime(day);

                    LocalDateTime nextDay = pivotLocalDateTime.plusDays(i + 1);
                    campingDateDTO.setCheckoutDateTime(nextDay);

                    campingDateDTO.setBooking(bookingDTO);

                    campingDateList.add(campingDateDTO);

                }
                pivotLocalDateTime = bookingDTO.getCheckoutDateTime();
            }

            for (int i = 0; i < ChronoUnit.DAYS.between(pivotLocalDateTime, checkoutDateTime); i++) {
                CampingDateDTO campingDateDTO = new CampingDateDTO();

                LocalDateTime day = pivotLocalDateTime.plusDays(i);
                campingDateDTO.setCheckInDateTime(day);

                LocalDateTime nextDay = pivotLocalDateTime.plusDays(i + 1);
                campingDateDTO.setCheckoutDateTime(nextDay);

                campingDateList.add(campingDateDTO);

            }
        }


        return campingDateList;
    }


}
