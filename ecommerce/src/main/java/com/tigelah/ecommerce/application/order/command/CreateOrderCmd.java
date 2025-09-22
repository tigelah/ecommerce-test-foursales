package com.tigelah.ecommerce.application.order.command;

import java.util.List;
import java.util.UUID;

public record CreateOrderCmd(UUID userId, List<Item> items) {
  public record Item(UUID productId, Integer quantity) {}
}
