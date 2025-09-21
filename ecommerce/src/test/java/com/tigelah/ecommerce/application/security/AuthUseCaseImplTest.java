package com.tigelah.ecommerce.application.security;

import com.tigelah.ecommerce.application.security.ports.JwtTokenService;
import com.tigelah.ecommerce.application.security.usecase.AuthUseCaseImpl;
import com.tigelah.ecommerce.domains.user.PasswordHasher;
import com.tigelah.ecommerce.domains.user.Role;
import com.tigelah.ecommerce.domains.user.User;
import com.tigelah.ecommerce.domains.user.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthUseCaseImplTest {

  @Test
  void deve_autenticar_e_gerar_token_quando_credenciais_validas() {
    UserRepository users = mock(UserRepository.class);
    PasswordHasher hasher = mock(PasswordHasher.class);
    JwtTokenService jwt = mock(JwtTokenService.class);

    var user = User.fromHashed(UUID.randomUUID(), "admin@acme.com", "HASH", Role.ADMIN);
    when(users.findByEmail("admin@acme.com")).thenReturn(Optional.of(user));
    when(hasher.matches("admin123", "HASH")).thenReturn(true);
    when(jwt.generate(anyString(), anyMap(), anyLong())).thenReturn("TOKEN");

    var usecase = new AuthUseCaseImpl(users, hasher, jwt, new SecurityProperties(3600));

    var token = usecase.login("admin@acme.com", "admin123");

    assertEquals("Bearer", token.tokenType());
    assertEquals("TOKEN", token.accessToken());
    assertEquals(3600, token.expiresInSeconds());
  }

  @Test
  void deve_falhar_quando_usuario_nao_existe() {
    UserRepository users = mock(UserRepository.class);
    PasswordHasher hasher = mock(PasswordHasher.class);
    JwtTokenService jwt = mock(JwtTokenService.class);
    when(users.findByEmail("x@x.com")).thenReturn(Optional.empty());

    var usecase = new AuthUseCaseImpl(users, hasher, jwt, new SecurityProperties(3600));

    assertThrows(IllegalArgumentException.class, () -> usecase.login("x@x.com", "123"));
  }

  @Test
  void deve_falhar_quando_senha_invalida() {
    UserRepository users = mock(UserRepository.class);
    PasswordHasher hasher = mock(PasswordHasher.class);
    JwtTokenService jwt = mock(JwtTokenService.class);
    var user = User.fromHashed(UUID.randomUUID(), "user@acme.com", "HASH", Role.USER);
    when(users.findByEmail("user@acme.com")).thenReturn(Optional.of(user));
    when(hasher.matches("wrong", "HASH")).thenReturn(false);

    var usecase = new AuthUseCaseImpl(users, hasher, jwt, new SecurityProperties(3600));

    assertThrows(IllegalArgumentException.class, () -> usecase.login("user@acme.com", "wrong"));
  }
}
