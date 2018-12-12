package com.upgrade.volcano.campsite.entities.repositories;

import com.upgrade.volcano.campsite.entities.Booking;
import com.upgrade.volcano.campsite.entities.Campsite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor {

    List<Booking> findAllByCampsite(Campsite campsite);

    List<Booking> findAllByCampsiteId(Long id);

    Booking findByCampsiteIdAndId(Long campsiteId, Long id);

}
