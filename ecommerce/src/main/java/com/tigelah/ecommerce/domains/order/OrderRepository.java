package com.tigelah.ecommerce.domains.order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
  Optional<Order> findById(UUID id);
  Order save(Order order);
}
