package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.entities.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
    Optional<Role> findById(Long id);
    List<Role> findAll();
    Role save(Role role);
}
