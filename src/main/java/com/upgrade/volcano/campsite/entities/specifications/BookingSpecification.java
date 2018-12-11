package com.upgrade.volcano.campsite.entities.specifications;

import com.upgrade.volcano.campsite.entities.Booking;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BookingSpecification implements Specification<Booking> {
        private Long campsiteId;

    public BookingSpecification(Long campsiteId) {
        this.campsiteId = campsiteId;
    }

    @Override
    public Predicate toPredicate(Root<Booking> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (campsiteId == null) {
            return cb.isTrue(cb.literal(true)); // always true = no filtering
        }
        return cb.equal(root.get("campsite").get("id"), this.campsiteId);
    }

}

