package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.SectionsDTO;
import com.hotelJB.hotelJB_API.models.entities.Sections;
import com.hotelJB.hotelJB_API.models.responses.SectionResponse;

import java.util.List;
import java.util.Optional;

public interface SectionsService {
    void save(SectionsDTO data) throws Exception;
    void update(SectionsDTO data, int sectionId) throws Exception;
    void delete(int sectionId) throws Exception;
    List<Sections> getAll();
    Optional<SectionResponse> findById(int sectionId, String language);
    List<SectionResponse> findByCategory(int categoryId, String language);
    List<SectionResponse> findByLanguage(String language);
}
