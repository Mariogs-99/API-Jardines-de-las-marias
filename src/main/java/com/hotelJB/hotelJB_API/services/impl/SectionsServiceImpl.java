package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.SectionsDTO;
import com.hotelJB.hotelJB_API.models.entities.Category;
import com.hotelJB.hotelJB_API.models.entities.Sections;
import com.hotelJB.hotelJB_API.models.responses.SectionResponse;
import com.hotelJB.hotelJB_API.repositories.CategoryRepository;
import com.hotelJB.hotelJB_API.repositories.SectionsRespositoru;
import com.hotelJB.hotelJB_API.services.SectionsService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectionsServiceImpl implements SectionsService {
    @Autowired
    private SectionsRespositoru sectionsRespository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Override
    public void save(SectionsDTO data) throws Exception {
        try {
            Category category = categoryRepository.findById(data.getCategoryId())
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Category"));

            Sections section = new Sections(data.getTitleEs(), data.getTitleEn(),
                    data.getDescriptionEs(), data.getDescriptionEn(), category);
            sectionsRespository.save(section);
        } catch (Exception e) {
            throw new Exception("Error save Section");
        }
    }

    @Override
    public void update(SectionsDTO data, int sectionId) throws Exception {
        Category category = categoryRepository.findById(data.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Category"));

        Sections section = sectionsRespository.findById(sectionId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Section"));

        section.setTitleEs(data.getTitleEs());
        section.setTitleEn(data.getTitleEn());
        section.setDescriptionEs(data.getDescriptionEs());
        section.setDescriptionEn(data.getDescriptionEn());
        section.setCategory(category);
        sectionsRespository.save(section);
    }

    @Override
    public void delete(int sectionId) throws Exception {
        Sections section = sectionsRespository.findById(sectionId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Section"));
        sectionsRespository.delete(section);
    }

    @Override
    public List<Sections> getAll() {
        return sectionsRespository.findAll();
    }

    @Override
    public Optional<SectionResponse> findById(int sectionId, String lang) {
        Optional<Sections> section = sectionsRespository.findById(sectionId);

        return section.map(value -> new SectionResponse(
                value.getSectionId(),
                lang.equals("es") ? value.getTitleEs() : value.getTitleEn(),
                lang.equals("es") ? value.getDescriptionEs() : value.getDescriptionEn(),
                value.getCategory().getCategoryId()));
    }

    @Override
    public List<SectionResponse> findByCategory(int categoryId, String lang) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Category"));

        List<Sections>sections = sectionsRespository.findByCategory(category);

        return sections.stream().map(value -> new SectionResponse(
                value.getSectionId(),
                lang.equals("es") ? value.getTitleEs() : value.getTitleEn(),
                lang.equals("es") ? value.getDescriptionEs() : value.getDescriptionEn(),
                value.getCategory().getCategoryId())).toList();
    }

    @Override
    public List<SectionResponse> findByLanguage(String language) {
        List<Sections> sections = sectionsRespository.findAll();

        return sections.stream().map(value -> new SectionResponse(
                value.getSectionId(),
                language.equals("es") ? value.getTitleEs() : value.getTitleEn(),
                language.equals("es") ? value.getDescriptionEs() : value.getDescriptionEn(),
                value.getCategory().getCategoryId())).toList();
    }
}
