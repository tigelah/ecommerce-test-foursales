package com.tigelah.ecommerce.infrastructure.security;

import com.tigelah.ecommerce.domains.user.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements PasswordHasher {
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  @Override
  public String hash(String rawPassword) {
    return encoder.encode(rawPassword);
  }

  @Override
  public boolean matches(String raw, String hash) {
    return encoder.matches(raw, hash);
  }
}
