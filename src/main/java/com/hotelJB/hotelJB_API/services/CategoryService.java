package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.CategoryDTO;
import com.hotelJB.hotelJB_API.models.entities.Category;
import com.hotelJB.hotelJB_API.models.responses.CategoryResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    void save(CategoryDTO data) throws Exception;
    void update(CategoryDTO data, int categoryId) throws Exception;
    void delete(int categoryId) throws Exception;
    List<Category> getAll();
    Optional<CategoryResponse> findById(int categoryId, String language);
    List<CategoryResponse> findByLanguage(String language);
}
