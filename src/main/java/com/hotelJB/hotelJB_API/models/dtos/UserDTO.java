package com.hotelJB.hotelJB_API.models.dtos;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Boolean active;

    // Nuevo campo para enviar el rol
    private String role;
}
