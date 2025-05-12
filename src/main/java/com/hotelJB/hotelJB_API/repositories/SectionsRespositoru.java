package com.hotelJB.hotelJB_API.repositories;

import com.hotelJB.hotelJB_API.models.entities.Category;
import com.hotelJB.hotelJB_API.models.entities.Sections;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionsRespositoru extends JpaRepository<Sections, Integer> {
    List<Sections> findByCategory(Category category);
}
