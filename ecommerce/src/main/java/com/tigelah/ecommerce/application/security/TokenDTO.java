package com.tigelah.ecommerce.application.security;

public record TokenDTO(String accessToken, String tokenType, long expiresInSeconds) {}
