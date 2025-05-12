package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.ImgDTO;
import com.hotelJB.hotelJB_API.models.entities.Gallery;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;

public interface GalleryService {
    void save(ImgDTO data) throws Exception;
    void update(ImgDTO data, int galleryId) throws Exception;
    void delete(int galleryId) throws Exception;
    List<Gallery> getAll();
    Optional<Gallery> findById(int galleryId);
    Resource getFileAsResourceById(int id);
    List<Gallery> findByCategory(int categoryId);

}

