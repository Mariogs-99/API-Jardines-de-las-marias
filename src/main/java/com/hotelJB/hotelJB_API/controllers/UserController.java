package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.models.entities.User_;
import com.hotelJB.hotelJB_API.services.UserService;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RequestErrorHandler errorHandler;

    @GetMapping("/")
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/by-token")
    public ResponseEntity<?> getByToken(@RequestBody String token){
        try {
            // Obtener el usuario autenticado directamente del servicio
            User_ user = userService.findUserAuthenticated();

            if (user == null) {
                return new ResponseEntity<>(new MessageDTO("Usuario no encontrado"), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Error al procesar la solicitud"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
