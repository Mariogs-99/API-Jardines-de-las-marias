package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.models.dtos.UserDTO;
import com.hotelJB.hotelJB_API.models.responses.UserResponse;
import com.hotelJB.hotelJB_API.models.entities.User_;
import com.hotelJB.hotelJB_API.services.UserService;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RequestErrorHandler errorHandler;

    // 🟢 Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 🟢 Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        try {
            UserResponse user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO(e.getMessage()));
        }
    }

    // 🟢 Crear nuevo usuario
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserDTO dto) {
        try {
            userService.createUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("Usuario creado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO(e.getMessage()));
        }
    }

    // 🟡 Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody UserDTO dto) {
        try {
            userService.updateUser(id, dto);
            return ResponseEntity.ok(new MessageDTO("Usuario actualizado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO(e.getMessage()));
        }
    }

    // 🔴 Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new MessageDTO("Usuario eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO(e.getMessage()));
        }
    }

    // 🧾 Obtener usuario autenticado por token
    @GetMapping("/by-token")
    public ResponseEntity<?> getByToken() {
        try {
            User_ user = userService.findUserAuthenticated();
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Usuario no encontrado"));
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageDTO("Error al procesar la solicitud"));
        }
    }
}
