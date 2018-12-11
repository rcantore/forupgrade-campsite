package com.upgrade.volcano.campsite.entities.specifications;

import com.upgrade.volcano.campsite.entities.Booking;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

public class BookingBetweenDates implements Specification<Booking> {
    private LocalDateTime checkInDateTime;
    private LocalDateTime checkoutDateTime;

    public BookingBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        this.checkInDateTime = startDate;
        this.checkoutDateTime = endDate;
    }


    @Override
    public Predicate toPredicate(Root<Booking> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (checkInDateTime == null && checkoutDateTime == null) {
            return cb.isTrue(cb.literal(true)); // always true = no filtering
        }

        return cb.and(
                cb.greaterThanOrEqualTo(root.get("checkInDateTime"), checkInDateTime),
                cb.lessThanOrEqualTo(root.get("checkoutDateTime"), checkoutDateTime)
        );
    }
}
