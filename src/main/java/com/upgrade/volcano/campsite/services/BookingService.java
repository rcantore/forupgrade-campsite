package com.upgrade.volcano.campsite.services;

import com.upgrade.volcano.campsite.dtos.BookingDTO;
import com.upgrade.volcano.campsite.entities.Booking;
import com.upgrade.volcano.campsite.entities.Campsite;
import com.upgrade.volcano.campsite.entities.ResultVO;
import com.upgrade.volcano.campsite.entities.repositories.BookingRepository;
import com.upgrade.volcano.campsite.entities.repositories.CampsiteRepository;
import com.upgrade.volcano.campsite.entities.specifications.BookingBetweenDates;
import com.upgrade.volcano.campsite.entities.specifications.BookingForCampsiteId;
import com.upgrade.volcano.campsite.validations.BookingInThePastValidation;
import com.upgrade.volcano.campsite.validations.BookingTooCloseValidation;
import com.upgrade.volcano.campsite.validations.BookingValidationCondition;
import com.upgrade.volcano.campsite.validations.DateRangeValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    CampsiteRepository campsiteRepository;

    private List<BookingValidationCondition> conditions;

    @PostConstruct
    public void init() {
        conditions = new ArrayList<>();
        conditions.add(new DateRangeValidation());
        conditions.add(new BookingInThePastValidation());
        conditions.add(new BookingTooCloseValidation());

    }

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

    public ResultVO createBookingForCampsiteId(BookingDTO bookingDTO, Long campsiteId) throws Exception {
        ResultVO resultVO = validateNewBooking(bookingDTO);

        if (resultVO.getErrors().isEmpty()) {
            Optional<Campsite> campsite = campsiteRepository.findById(campsiteId);
            BookingDTO returnBooking = new BookingDTO();
            if (campsite.isPresent()) {
                Specification<Booking> spec = Specification.where(new BookingForCampsiteId(campsiteId))
                        .and(new BookingBetweenDates(bookingDTO.getCheckInDateTime(), bookingDTO.getCheckoutDateTime()));
                List<BookingDTO> bookingList = this.getAllBookings(spec);

                if (!bookingList.isEmpty()) {
                    // date not available error
                    resultVO.getErrors().add("The date you provided is not available for booking");
                } else {

                    ModelMapper modelMapper = new ModelMapper();
                    Booking booking = modelMapper.map(bookingDTO, Booking.class);

                    Campsite availableCampsite = campsite.get();
                    booking.setCampsite(availableCampsite);

                    bookingRepository.save(booking);
                    availableCampsite.getBookings().add(booking);
                    campsiteRepository.save(availableCampsite);

                    returnBooking = modelMapper.map(booking, BookingDTO.class);

                    resultVO.getData().put("return", returnBooking);
                }
            }
        }

        return resultVO;
    }

    private ResultVO validateNewBooking(BookingDTO bookingDTO) {
        ResultVO resultVO = new ResultVO();
        resultVO.setErrors(new ArrayList<>());

        conditions.forEach(bookingValidationCondition -> {
            Optional<String> error = bookingValidationCondition.validate(bookingDTO);
            error.ifPresent(s -> resultVO.getErrors().add(s));
        });

        return resultVO;

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
