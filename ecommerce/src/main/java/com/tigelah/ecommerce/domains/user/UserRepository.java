package com.tigelah.ecommerce.domains.user;

import java.util.Optional;

public interface UserRepository {
  Optional<User> findByEmail(String email);
  User save(User user);
}
