package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.RestaurantDTO;
import com.hotelJB.hotelJB_API.models.entities.Restaurant;
import com.hotelJB.hotelJB_API.models.responses.RestaurantResponse;
import com.hotelJB.hotelJB_API.repositories.RestaurantRepository;
import com.hotelJB.hotelJB_API.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";
    private final String PDF_DIR = System.getProperty("user.dir") + "/menu";

    @Override
    public RestaurantResponse create(RestaurantDTO dto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setDescription(dto.getDescription());
        restaurant.setSchedule(dto.getSchedule());
        restaurant.setNameEn(dto.getNameEn());
        restaurant.setDescriptionEn(dto.getDescriptionEn());
        restaurant.setScheduleEn(dto.getScheduleEn());
        restaurant.setPdfMenuUrl(dto.getPdfMenuUrl());
        restaurant.setImgUrl(dto.getImgUrl());
        restaurant.setHighlighted(dto.isHighlighted());

        repository.save(restaurant);
        return toResponse(restaurant);
    }

    @Override
    public RestaurantResponse createWithFiles(
            String name, String description, String schedule,
            String nameEn, String descriptionEn, String scheduleEn,
            boolean highlighted, MultipartFile image, MultipartFile pdf) {

        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setDescription(description);
        restaurant.setSchedule(schedule);
        restaurant.setNameEn(nameEn);
        restaurant.setDescriptionEn(descriptionEn);
        restaurant.setScheduleEn(scheduleEn);
        restaurant.setHighlighted(highlighted);

        if (image != null && !image.isEmpty()) {
            saveFile(image, UPLOAD_DIR);
            restaurant.setImgUrl("/uploads/" + image.getOriginalFilename());
        }

        if (pdf != null && !pdf.isEmpty()) {
            saveFile(pdf, PDF_DIR);
            restaurant.setPdfMenuUrl("/menu/" + pdf.getOriginalFilename());
        }

        repository.save(restaurant);
        return toResponse(restaurant);
    }

    @Override
    public RestaurantResponse updateWithFiles(
            Long id, String name, String description, String schedule,
            String nameEn, String descriptionEn, String scheduleEn,
            boolean highlighted, MultipartFile image, MultipartFile pdf) {

        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado con id: " + id));

        restaurant.setName(name);
        restaurant.setDescription(description);
        restaurant.setSchedule(schedule);
        restaurant.setNameEn(nameEn);
        restaurant.setDescriptionEn(descriptionEn);
        restaurant.setScheduleEn(scheduleEn);
        restaurant.setHighlighted(highlighted);

        if (image != null && !image.isEmpty()) {
            saveFile(image, UPLOAD_DIR);
            restaurant.setImgUrl("/uploads/" + image.getOriginalFilename());
        }

        if (pdf != null && !pdf.isEmpty()) {
            saveFile(pdf, PDF_DIR);
            restaurant.setPdfMenuUrl("/menu/" + pdf.getOriginalFilename());
        }

        repository.save(restaurant);
        return toResponse(restaurant);
    }

    @Override
    public List<RestaurantResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<RestaurantResponse> getHighlighted() {
        return repository.findByHighlightedTrue().stream().map(this::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private String saveFile(MultipartFile file, String folderPath) {
        try {
            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filePath = folderPath + "/" + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar archivo: " + file.getOriginalFilename(), e);
        }
    }

    private RestaurantResponse toResponse(Restaurant r) {
        RestaurantResponse res = new RestaurantResponse();
        res.setRestaurantId(r.getRestaurantId());
        res.setName(r.getName());
        res.setDescription(r.getDescription());
        res.setSchedule(r.getSchedule());
        res.setNameEn(r.getNameEn());
        res.setDescriptionEn(r.getDescriptionEn());
        res.setScheduleEn(r.getScheduleEn());
        res.setPdfMenuUrl(r.getPdfMenuUrl());
        res.setImgUrl(r.getImgUrl());
        res.setHighlighted(r.isHighlighted());
        return res;
    }
}
