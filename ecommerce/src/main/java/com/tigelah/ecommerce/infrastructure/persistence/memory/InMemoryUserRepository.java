package com.tigelah.ecommerce.infrastructure.persistence.memory;

import com.tigelah.ecommerce.domains.user.PasswordHasher;
import com.tigelah.ecommerce.domains.user.Role;
import com.tigelah.ecommerce.domains.user.User;
import com.tigelah.ecommerce.domains.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {

  private final PasswordHasher hasher;
  private final Map<String, User> byEmail = new HashMap<>();

  @PostConstruct
  void seed() {
    byEmail.put("admin@acme.com", User.createWithRawPassword(null, "admin@acme.com", "admin123", Role.ADMIN, hasher));
    byEmail.put("user@acme.com", User.createWithRawPassword(null, "user@acme.com", "user123", Role.USER, hasher));
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(byEmail.get(email));
  }

  @Override
  public User save(User user) {
    byEmail.put(user.getEmail(), user);
    return user;
  }
}
