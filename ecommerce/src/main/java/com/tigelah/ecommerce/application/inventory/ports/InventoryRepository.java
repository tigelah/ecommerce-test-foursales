package com.tigelah.ecommerce.application.inventory.ports;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository {

  Optional<ProductSnapshot> findProductForUpdate(UUID id);

  void saveProduct(ProductSnapshot p);

  boolean wasProcessed(UUID orderId);

  void markProcessed(UUID orderId, String type);

  record ProductSnapshot(UUID id, String name, java.math.BigDecimal price, BigInteger stock) {
  }
}
