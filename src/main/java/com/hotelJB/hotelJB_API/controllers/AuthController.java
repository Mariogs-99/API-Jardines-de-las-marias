package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.LoginDTO;
import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.models.dtos.SingupDTO;
import com.hotelJB.hotelJB_API.models.dtos.TokenDTO;
import com.hotelJB.hotelJB_API.models.entities.Token;
import com.hotelJB.hotelJB_API.models.entities.User_;
import com.hotelJB.hotelJB_API.services.UserService;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private RequestErrorHandler errorHandler;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO data, BindingResult validations) {
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try {
            var loginResponse = userService.loginWithToken(data);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.equals("Credenciales inválidas")) {
                return new ResponseEntity<>(new MessageDTO(msg), HttpStatus.UNAUTHORIZED);
            } else if (msg.equals("Tu cuenta está inactiva. Contacta al administrador.")) {
                return new ResponseEntity<>(new MessageDTO(msg), HttpStatus.CONFLICT);
            } else {
                e.printStackTrace();
                return new ResponseEntity<>(new MessageDTO("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }



    @PostMapping("/singup")
    public ResponseEntity<?> singup(@RequestBody @Valid SingupDTO user, BindingResult validations) {
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        List<User_> allUsers = userService.findAll();
        List<String> allUsersUsername = allUsers.stream().map(u -> u.getUsername()).collect(Collectors.toList());
        if (allUsersUsername.contains(user.getUsername()))
            return new ResponseEntity<>(new MessageDTO("User already exists"), HttpStatus.CONFLICT);

        try {
            userService.save(user);
            return new ResponseEntity<>(new MessageDTO("User created"), HttpStatus.CREATED);

        } catch (Exception e) {
            if (e.getMessage().equals("User already exists")) {
                return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.CONFLICT);
            } else {
                e.printStackTrace();
                return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        User_ userFound = userService.findUserAuthenticated();
        if (userFound == null)
            return new ResponseEntity<>(new MessageDTO("User not authenticated"), HttpStatus.NOT_FOUND);

        try {
            userService.cleanTokens(userFound);
            return new ResponseEntity<>(new MessageDTO("User logged out"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
