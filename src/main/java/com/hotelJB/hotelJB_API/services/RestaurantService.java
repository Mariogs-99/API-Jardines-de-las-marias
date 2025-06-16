package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.RestaurantDTO;
import com.hotelJB.hotelJB_API.models.responses.RestaurantResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RestaurantService {
    RestaurantResponse create(RestaurantDTO dto);

    RestaurantResponse createWithFiles(
            String name, String description, String schedule,
            String nameEn, String descriptionEn, String scheduleEn,
            boolean highlighted, MultipartFile image, MultipartFile pdf);

    RestaurantResponse updateWithFiles(
            Long id, String name, String description, String schedule,
            String nameEn, String descriptionEn, String scheduleEn,
            boolean highlighted, MultipartFile image, MultipartFile pdf);

    List<RestaurantResponse> getAll();
    List<RestaurantResponse> getHighlighted();


    void delete(Long id);


}
