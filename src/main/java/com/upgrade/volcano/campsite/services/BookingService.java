package com.upgrade.volcano.campsite.services;

import com.upgrade.volcano.campsite.dtos.BookingDTO;
import com.upgrade.volcano.campsite.entities.Booking;
import com.upgrade.volcano.campsite.entities.Campsite;
import com.upgrade.volcano.campsite.entities.repositories.BookingRepository;
import com.upgrade.volcano.campsite.entities.repositories.CampsiteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    CampsiteRepository campsiteRepository;

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

    public void createBookingForCampsiteId(BookingDTO bookingDTO, Long campsiteId) {
        Optional<Campsite> campsite = campsiteRepository.findById(campsiteId);
        if (campsite.isPresent()) {
            ModelMapper modelMapper = new ModelMapper();
            Booking booking = modelMapper.map(bookingDTO, Booking.class);

            Campsite availableCampsite = campsite.get();
            booking.setCampsite(availableCampsite);

            bookingRepository.save(booking);
            availableCampsite.getBookings().add(booking);
            campsiteRepository.save(availableCampsite);
        }

    }

    public void updateBookingForCampsiteId(BookingDTO bookingDTO, Long campsiteId) {
        Optional<Campsite> campsite = campsiteRepository.findById(campsiteId);
        if (campsite.isPresent()) {
            ModelMapper modelMapper = new ModelMapper();
            Booking mappedBooking = modelMapper.map(bookingDTO, Booking.class);

            Campsite availableCampsite = campsite.get();
            mappedBooking.setCampsite(availableCampsite);

            bookingRepository.save(mappedBooking);

        }

    }

    public void deleteBookingForCampsiteId(Long bookingId, Long campsiteId) {
        Optional<Campsite> campsite = campsiteRepository.findById(campsiteId);
        if (campsite.isPresent()) {
            Campsite availableCampsite = campsite.get();

            availableCampsite.getBookings().removeIf(booking -> booking.getId() == bookingId);
            campsiteRepository.save(availableCampsite);

            bookingRepository.deleteById(bookingId);

        }

    }
}
