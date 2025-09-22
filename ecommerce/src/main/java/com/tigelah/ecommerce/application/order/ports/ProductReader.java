package com.tigelah.ecommerce.application.order.ports;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ProductReader {
  Optional<ProductSnapshot> getById(UUID id);

  record ProductSnapshot(UUID id, String name, BigDecimal price, int stock) {
  }
}
