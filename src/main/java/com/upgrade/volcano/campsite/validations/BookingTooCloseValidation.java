package com.upgrade.volcano.campsite.validations;

import com.upgrade.volcano.campsite.dtos.BookingDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class BookingTooCloseValidation implements BookingValidationCondition {

    @Override
    public Optional<String> validate(BookingDTO bookingDTO) {
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.of(12,00));

        boolean withinOneDay = !bookingDTO.getCheckInDateTime().isAfter(today.plusDays(1));
        boolean pastOneMonth = !bookingDTO.getCheckInDateTime().isBefore(today.plusMonths(1));
        if (withinOneDay || pastOneMonth) {
            return Optional.of("Booking should be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance");
        }

        return Optional.empty();
    }
}
