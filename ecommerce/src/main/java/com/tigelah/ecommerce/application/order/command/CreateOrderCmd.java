package com.tigelah.ecommerce.application.order.command;

import java.util.List;
import java.util.UUID;

public record CreateOrderCmd(List<Item> items) {
  public record Item(UUID productId, Integer quantity) {}
}
