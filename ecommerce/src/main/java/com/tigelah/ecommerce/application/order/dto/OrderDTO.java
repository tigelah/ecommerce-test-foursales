package com.tigelah.ecommerce.application.order.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderDTO(UUID id, UUID userId, List<Item> items, String status, BigDecimal total) {
  public record Item(UUID productId, int quantity, BigDecimal unitPrice) {
  }
}
