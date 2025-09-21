package com.tigelah.ecommerce.domains.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

  private final UUID id;
  private final String email;
  private final String passwordHash;
  private final Role role;


  public static User createWithRawPassword(UUID id, String email, String rawPassword, Role role, PasswordHasher hasher) {
    Objects.requireNonNull(email, "email é obrigatório");
    Objects.requireNonNull(rawPassword, "rawPassword é obrigatório");
    Objects.requireNonNull(role, "role é obrigatório");
    Objects.requireNonNull(hasher, "hasher é obrigatório");

    String hashed = hasher.hash(rawPassword);
    return new User(id == null ? UUID.randomUUID() : id, email, hashed, role);
  }

  public static User fromHashed(UUID id, String email, String passwordHash, Role role) {
    Objects.requireNonNull(email, "email é obrigatório");
    Objects.requireNonNull(passwordHash, "passwordHash é obrigatório");
    Objects.requireNonNull(role, "role é obrigatório");

    return new User(id == null ? UUID.randomUUID() : id, email, passwordHash, role);
  }

  public boolean verifyPassword(String rawPassword, PasswordHasher hasher) {
    Objects.requireNonNull(hasher, "hasher é obrigatório");
    return hasher.matches(rawPassword, this.passwordHash);
  }
}
