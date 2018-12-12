package com.upgrade.volcano.campsite.validations;

import com.upgrade.volcano.campsite.dtos.BookingDTO;

import java.time.LocalDateTime;
import java.util.Optional;

public class BookingInThePastValidation implements BookingValidationCondition {

    @Override
    public Optional<String> validate(BookingDTO bookingDTO) {
        if (bookingDTO.getCheckInDateTime().isBefore(LocalDateTime.now())) {
            return Optional.of("Bookings must be scheduled in a future date");
        }
        return Optional.empty();
    }
}
