package com.application.seguridad.unir.dto;

import lombok.Data;

//Esta clase nos devolvera la informacion con el token y el tipo de token
@Data
public class AuthResponseDto {
    private final String accessToken;
    private final String tokenType = "Bearer ";

    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
