package com.tigelah.ecommerce.application.security.usecase;

import com.tigelah.ecommerce.application.security.SecurityProperties;
import com.tigelah.ecommerce.application.security.ports.JwtTokenService;
import com.tigelah.ecommerce.application.security.ports.TokenDTO;
import com.tigelah.ecommerce.domains.user.PasswordHasher;
import com.tigelah.ecommerce.domains.user.UserRepository;

import com.tigelah.ecommerce.domains.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthUseCaseImpl implements AuthUseCase {

  private final UserRepository users;
  private final PasswordHasher hasher;
  private final JwtTokenService jwt;
  private final SecurityProperties props; // ✅ agora é o top-level

  @Override
  public TokenDTO login(String email, String password) {
    User user = users.findByEmail(email)
      .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));

    if (!user.verifyPassword(password, hasher)) {
      throw new IllegalArgumentException("Credenciais inválidas");
    }

    String token = jwt.generate(
      user.getId().toString(),
      Map.of("role", user.getRole().name(), "email", user.getEmail()),
      props.jwtTtlSeconds()
    );
    return new TokenDTO(token, "Bearer", props.jwtTtlSeconds());
  }
}
