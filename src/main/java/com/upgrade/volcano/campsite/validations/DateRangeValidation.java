package com.upgrade.volcano.campsite.validations;

import com.upgrade.volcano.campsite.dtos.BookingDTO;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class DateRangeValidation implements BookingValidationCondition {

    @Override
    public Optional<String> validate(BookingDTO bookingDTO) {
        if (ChronoUnit.DAYS.between(bookingDTO.getCheckInDateTime(), bookingDTO.getCheckoutDateTime()) > 3) {
            return Optional.of("Bookings can't be for more than 3 days");
        }
        return Optional.empty();
    }
}
