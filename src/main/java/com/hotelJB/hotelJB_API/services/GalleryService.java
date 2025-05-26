package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.GalleryDTO;
import com.hotelJB.hotelJB_API.models.dtos.ImgDTO;
import com.hotelJB.hotelJB_API.models.entities.Gallery;
import com.hotelJB.hotelJB_API.models.responses.GalleryResponse;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;

public interface GalleryService {
    List<GalleryResponse> getAll();
    List<GalleryResponse> getPublicGallery();
    GalleryResponse save(GalleryDTO dto, String imageUrl);
    GalleryResponse update(Long id, GalleryDTO dto);
    void delete(Long id);
}


