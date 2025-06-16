package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.ExperienceDTO;
import com.hotelJB.hotelJB_API.models.entities.Experience;
import com.hotelJB.hotelJB_API.models.responses.ExperienceResponse;
import com.hotelJB.hotelJB_API.repositories.ExperienceRepository;
import com.hotelJB.hotelJB_API.services.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository repository;

    @Override
    public ExperienceResponse create(ExperienceDTO dto) {
        Experience experience = Experience.builder()
                .titleEs(dto.getTitleEs())
                .titleEn(dto.getTitleEn())
                .descriptionEs(dto.getDescriptionEs())
                .descriptionEn(dto.getDescriptionEn())
                .duration(dto.getDuration())
                .capacity(dto.getCapacity())
                .price(dto.getPrice())
                .availableDaysEs(dto.getAvailableDaysEs())
                .availableDaysEn(dto.getAvailableDaysEn())
                .imageUrl(dto.getImageUrl())
                .active(dto.getActive() != null ? dto.getActive() : true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return toResponse(repository.save(experience), "es");
    }

    @Override
    public ExperienceResponse update(Long id, ExperienceDTO dto) {
        Experience experience = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Experience not found"));

        experience.setTitleEs(dto.getTitleEs());
        experience.setTitleEn(dto.getTitleEn());
        experience.setDescriptionEs(dto.getDescriptionEs());
        experience.setDescriptionEn(dto.getDescriptionEn());
        experience.setDuration(dto.getDuration());
        experience.setCapacity(dto.getCapacity());
        experience.setPrice(dto.getPrice());
        experience.setAvailableDaysEs(dto.getAvailableDaysEs());
        experience.setAvailableDaysEn(dto.getAvailableDaysEn());
        experience.setImageUrl(dto.getImageUrl());
        experience.setActive(dto.getActive());
        experience.setUpdatedAt(LocalDateTime.now());

        return toResponse(repository.save(experience), "es");
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }


    @Override
    public List<ExperienceResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponseFull) // 👈 esta versión retorna todo
                .collect(Collectors.toList());
    }

    private ExperienceResponse toResponseFull(Experience exp) {
        return ExperienceResponse.builder()
                .experienceId(exp.getExperienceId())
                .titleEs(exp.getTitleEs())
                .titleEn(exp.getTitleEn())
                .descriptionEs(exp.getDescriptionEs())
                .descriptionEn(exp.getDescriptionEn())
                .duration(exp.getDuration())
                .capacity(exp.getCapacity())
                .price(exp.getPrice())
                .availableDaysEs(exp.getAvailableDaysEs())
                .availableDaysEn(exp.getAvailableDaysEn())
                .imageUrl(exp.getImageUrl())
                .active(exp.getActive())
                .build();
    }



    @Override
    public ExperienceResponse getById(Long id) {
        Experience experience = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Experience not found"));
        return toResponse(experience, "es");
    }

    @Override
    public List<ExperienceResponse> getAllPublic(String lang) {
        return repository.findByActiveTrue()
                .stream()
                .map(exp -> toResponse(exp, lang))
                .collect(Collectors.toList());
    }

    // Método auxiliar para seleccionar idioma
    private ExperienceResponse toResponse(Experience exp, String lang) {
        return ExperienceResponse.builder()
                .experienceId(exp.getExperienceId())
                .title(lang.equals("en") ? exp.getTitleEn() : exp.getTitleEs())
                .description(lang.equals("en") ? exp.getDescriptionEn() : exp.getDescriptionEs())
                .duration(exp.getDuration())
                .capacity(exp.getCapacity())
                .price(exp.getPrice())
                .availableDays(lang.equals("en") ? exp.getAvailableDaysEn() : exp.getAvailableDaysEs())
                .imageUrl(exp.getImageUrl())
                .active(exp.getActive())
                .build();
    }
}
