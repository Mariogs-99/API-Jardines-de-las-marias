package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.ImgDTO;
import com.hotelJB.hotelJB_API.models.entities.Img;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;

public interface ImgService {
    void save(ImgDTO data) throws Exception;
    void update(ImgDTO data, int imgId) throws Exception;
    void delete(int imgId) throws Exception;
    List<Img> getAll();
    Optional<Img> findById(int imgId);
    Resource getFileAsResourceById(int id);
}
