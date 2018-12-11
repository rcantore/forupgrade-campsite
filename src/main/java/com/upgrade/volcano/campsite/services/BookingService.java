package com.upgrade.volcano.campsite.services;

import com.upgrade.volcano.campsite.dtos.BookingDTO;
import com.upgrade.volcano.campsite.dtos.CampsiteDTO;
import com.upgrade.volcano.campsite.entities.Booking;
import com.upgrade.volcano.campsite.entities.Campsite;
import com.upgrade.volcano.campsite.entities.repositories.BookingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;

    public List<BookingDTO> getAllBookingsForCampsiteId(Long id) {
        ModelMapper modelMapper = new ModelMapper();

        List<BookingDTO> bookingsDTO = new ArrayList<>();
        List<Booking> bookingList = bookingRepository.findAllByCampsiteId(id);
        bookingList.forEach(booking -> bookingsDTO.add(modelMapper.map(booking, BookingDTO.class)));

        return bookingsDTO;
    }

    public List<BookingDTO> getAllBookings() {
        ModelMapper modelMapper = new ModelMapper();

        List<BookingDTO> bookingsDTO = new ArrayList<>();
        List<Booking> bookingList = bookingRepository.findAll();
        bookingList.forEach(booking -> bookingsDTO.add(modelMapper.map(booking, BookingDTO.class)));

        return bookingsDTO;
    }

    public List<BookingDTO> getAllBookings(Specification<Booking> specification) {
        ModelMapper modelMapper = new ModelMapper();

        List<BookingDTO> bookingsDTO = new ArrayList<>();
        List<Booking> bookingList = bookingRepository.findAll(specification);
        bookingList.forEach(booking -> bookingsDTO.add(modelMapper.map(booking, BookingDTO.class)));

        return bookingsDTO;
    }
}
