package com.hotelJB.hotelJB_API.services;


import com.hotelJB.hotelJB_API.models.dtos.ExperienceDTO;
import com.hotelJB.hotelJB_API.models.responses.ExperienceResponse;

import java.util.List;

public interface ExperienceService {
    ExperienceResponse create(ExperienceDTO dto);
    ExperienceResponse update(Long id, ExperienceDTO dto);
    void delete(Long id);
    List<ExperienceResponse> getAll();
    ExperienceResponse getById(Long id);

    // ✅ NUEVO MÉTODO PÚBLICO PARA EL FRONTEND
    List<ExperienceResponse> getAllPublic(String lang);
}
