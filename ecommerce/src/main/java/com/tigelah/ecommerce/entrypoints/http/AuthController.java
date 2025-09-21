package com.tigelah.ecommerce.entrypoints.http;

import com.tigelah.ecommerce.application.security.ports.TokenDTO;
import com.tigelah.ecommerce.application.security.usecase.AuthUseCase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthUseCase auth;

  public AuthController(AuthUseCase auth) { this.auth = auth; }

  public record LoginRequest(@Email String email, @NotBlank String password) {}

  @PostMapping("/login")
  public ResponseEntity<TokenDTO> login(@RequestBody LoginRequest req) {
    return ResponseEntity.ok(auth.login(req.email(), req.password()));
  }
}
