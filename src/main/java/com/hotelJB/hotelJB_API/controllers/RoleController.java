package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.RoleDTO;
import com.hotelJB.hotelJB_API.models.entities.Role;
import com.hotelJB.hotelJB_API.models.responses.RoleResponse;
import com.hotelJB.hotelJB_API.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public RoleResponse createRole(@RequestBody @Valid RoleDTO dto) {
        Role role = new Role();
        role.setName(dto.getName());
        Role saved = roleService.save(role);
        return new RoleResponse(saved.getRoleId(), saved.getName());
    }

    @GetMapping
    public List<RoleResponse> getAllRoles() {
        return roleService.findAll()
                .stream()
                .map(r -> new RoleResponse(r.getRoleId(), r.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RoleResponse getById(@PathVariable Long id) {
        Role role = roleService.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        return new RoleResponse(role.getRoleId(), role.getName());
    }
}
