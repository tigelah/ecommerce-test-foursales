package com.tigelah.ecommerce.application.security;

public interface AuthUseCase {
  TokenDTO login(String email, String password);
}
