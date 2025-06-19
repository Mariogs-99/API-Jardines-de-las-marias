package com.hotelJB.hotelJB_API.models.responses;

import lombok.Data;

@Data
public class UserResponse {
    private int userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Boolean active;

    // Nuevo campo para el nombre del rol
    private String role;
}
