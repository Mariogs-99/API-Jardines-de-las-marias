package com.hotelJB.hotelJB_API.models.dtos;

import com.hotelJB.hotelJB_API.models.entities.Token;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDTO {
    private String token;

    public TokenDTO(Token token) {
        this.token = token.getToken();
    }
}
