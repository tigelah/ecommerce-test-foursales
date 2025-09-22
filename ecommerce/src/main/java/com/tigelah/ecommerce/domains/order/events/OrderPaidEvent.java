package com.tigelah.ecommerce.domains.order.events;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderPaidEvent(
  UUID orderId,
  UUID userId,
  List<Item> items,
  Instant paidAt,
  String eventType,
  int version
) {
  public record Item(UUID productId, int quantity) {}
  public static String type(){ return "order.paid"; }
}
