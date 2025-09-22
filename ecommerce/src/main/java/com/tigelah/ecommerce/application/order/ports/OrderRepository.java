package com.tigelah.ecommerce.application.order.ports;

import com.tigelah.ecommerce.domains.order.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
  Order save(Order order);
  Optional<Order> findById(UUID id);
  void update(Order order);
}
