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
                .title(dto.getTitle())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .capacity(dto.getCapacity())
                .price(dto.getPrice())
                .availableDays(dto.getAvailableDays())
                .imageUrl(dto.getImageUrl())
                .active(dto.getActive() != null ? dto.getActive() : true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return toResponse(repository.save(experience));
    }

    @Override
    public ExperienceResponse update(Long id, ExperienceDTO dto) {
        Experience experience = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Experience not found"));
        experience.setTitle(dto.getTitle());
        experience.setDescription(dto.getDescription());
        experience.setDuration(dto.getDuration());
        experience.setCapacity(dto.getCapacity());
        experience.setPrice(dto.getPrice());
        experience.setAvailableDays(dto.getAvailableDays());
        experience.setImageUrl(dto.getImageUrl());
        experience.setActive(dto.getActive());
        experience.setUpdatedAt(LocalDateTime.now());
        return toResponse(repository.save(experience));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<ExperienceResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExperienceResponse getById(Long id) {
        Experience experience = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Experience not found"));
        return toResponse(experience);
    }

    private ExperienceResponse toResponse(Experience exp) {
        return ExperienceResponse.builder()
                .experienceId(exp.getExperienceId())
                .title(exp.getTitle())
                .description(exp.getDescription())
                .duration(exp.getDuration())
                .capacity(exp.getCapacity())
                .price(exp.getPrice())
                .availableDays(exp.getAvailableDays())
                .imageUrl(exp.getImageUrl())
                .active(exp.getActive())
                .build();
    }
}
