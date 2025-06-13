// ContactMessageDTO.java
package com.hotelJB.hotelJB_API.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessageDTO {
    private String name;
    private String phone;
    private String email;
    private String brand;
    private String dates;
    private String budget;
    private String message;
}
