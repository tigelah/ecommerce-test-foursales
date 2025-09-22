package com.tigelah.ecommerce.application.order.ports;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ProductReader {
  record ProductSnapshot(UUID id, String name, BigDecimal price, int stock) {}
  Optional<ProductSnapshot> getById(UUID id);
}
