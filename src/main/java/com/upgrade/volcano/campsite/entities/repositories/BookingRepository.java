package com.upgrade.volcano.campsite.entities.repositories;

import com.upgrade.volcano.campsite.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
