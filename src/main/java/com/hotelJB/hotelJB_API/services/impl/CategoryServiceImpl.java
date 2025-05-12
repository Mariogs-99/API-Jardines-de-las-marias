package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.CategoryDTO;
import com.hotelJB.hotelJB_API.models.entities.Category;
import com.hotelJB.hotelJB_API.models.responses.CategoryResponse;
import com.hotelJB.hotelJB_API.repositories.CategoryRepository;
import com.hotelJB.hotelJB_API.services.CategoryService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Override
    public void save(CategoryDTO data) throws Exception {
        try{
            Category categoryRoom = new Category(data.getNameEs(),data.getNameEn(),data.getImg());
            categoryRepository.save(categoryRoom);
        }catch (Exception e){
            throw new Exception("Error save Category");
        }
    }

    @Override
    public void update(CategoryDTO data, int categoryId) throws Exception {
        try{
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "category"));

            category.setNameEs(data.getNameEs());
            category.setNameEn(data.getNameEn());
            category.setImg(data.getImg());
            categoryRepository.save(category);
        }catch (Exception e){
            throw new Exception("Error update Detail Room");
        }
    }

    @Override
    public void delete(int categoryId) throws Exception {
        try{
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Category"));

            categoryRepository.delete(category);
        }catch (Exception e){
            throw new Exception("Error delete category");
        }
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<CategoryResponse> findById(int categoryId, String lang) {
        Optional<Category> category = categoryRepository.findById(categoryId);

        return category.map(value -> new CategoryResponse(
                value.getCategoryId(),
                "es".equals(lang) ? value.getNameEs() : value.getNameEn(),
                value.getImg()
        ));
    }

    @Override
    public List<CategoryResponse> findByLanguage(String language) {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(category -> {
            // Selecciona el nombre dinámicamente según el idioma
            String name = "es".equals(language) ? category.getNameEs() : category.getNameEn();

            return new CategoryResponse(
                    category.getCategoryId(),
                    name,
                    category.getImg()
            );
        }).collect(Collectors.toList());
    }
}
