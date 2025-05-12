package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.MenuDTO;
import com.hotelJB.hotelJB_API.models.entities.Menu;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    void save(Menu data) throws Exception;
    void update(Menu data, int menuId) throws Exception;
    void delete(int menuId) throws Exception;
    List<Menu> getAll();
    Optional<Menu> findById(int menuId);
    Resource getFileAsResourceById(int id);
}
