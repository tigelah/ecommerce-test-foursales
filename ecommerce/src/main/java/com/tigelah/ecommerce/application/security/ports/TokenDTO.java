package com.tigelah.ecommerce.application.security.ports;

public record TokenDTO(String accessToken, String tokenType, long expiresInSeconds) {
}
