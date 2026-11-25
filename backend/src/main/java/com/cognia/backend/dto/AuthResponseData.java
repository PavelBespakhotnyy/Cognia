package com.cognia.backend.dto;

public record AuthResponseData(
        String accessToken,
        String tokenType,
        long expiresIn,
        UserResponse user) {

}
