package com.hotelJB.hotelJB_API.repositories;

import com.hotelJB.hotelJB_API.models.entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByActiveTrue();
}
