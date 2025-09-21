package com.tigelah.ecommerce.application.security.usecase;

import com.tigelah.ecommerce.application.security.ports.TokenDTO;

public interface AuthUseCase {
  TokenDTO login(String email, String password);
}
