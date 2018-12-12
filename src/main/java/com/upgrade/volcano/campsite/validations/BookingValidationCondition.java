package com.upgrade.volcano.campsite.validations;

import com.upgrade.volcano.campsite.dtos.BookingDTO;

import java.util.Optional;

@FunctionalInterface
public interface BookingValidationCondition {
    Optional<String> validate(final BookingDTO bookingDTO);
}
