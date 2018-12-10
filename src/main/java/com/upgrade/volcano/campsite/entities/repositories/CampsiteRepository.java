package com.upgrade.volcano.campsite.entities.repositories;

import com.upgrade.volcano.campsite.entities.Campsite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampsiteRepository extends JpaRepository<Campsite, Long> {

}
