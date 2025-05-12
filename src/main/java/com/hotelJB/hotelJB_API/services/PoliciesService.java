package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.PoliciesDTO;
import com.hotelJB.hotelJB_API.models.entities.Policies;
import com.hotelJB.hotelJB_API.models.responses.PoliciesResponse;

import java.util.List;
import java.util.Optional;

public interface PoliciesService {
    void save(PoliciesDTO data) throws Exception;
    void update(PoliciesDTO data, int policiesId) throws Exception;
    void delete(int policiesId) throws Exception;
    List<Policies> getAll();
    Optional<PoliciesResponse> findById(int policiesId, String language);
    List<PoliciesResponse> findByLanguage(String language);
}
