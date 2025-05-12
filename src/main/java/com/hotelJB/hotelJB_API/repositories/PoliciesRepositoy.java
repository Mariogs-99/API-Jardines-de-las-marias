package com.hotelJB.hotelJB_API.repositories;

import com.hotelJB.hotelJB_API.models.entities.Policies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoliciesRepositoy extends JpaRepository<Policies, Integer> {
}
