package com.tigelah.ecommerce.domains.order;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Getter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Setter
public class Order {

  private final UUID id;
  private final UUID userId;
  private final List<OrderItem> items;
  private final Instant createdAt;
  private OrderStatus status;
  private BigDecimal total;
  private Instant paidAt;


  public static Order of(UUID userId, List<OrderItem> items) {
    Objects.requireNonNull(userId, "userId é obrigatório");
    Objects.requireNonNull(items, "items é obrigatório");
    var safeItems = new ArrayList<>(items);
    var now = Instant.now();
    var total = computeTotal(safeItems);
    var order = new Order(UUID.randomUUID(), userId, safeItems, now, OrderStatus.PENDENTE, total,null);
    order.validate();
    return order;
  }

  private static BigDecimal computeTotal(List<OrderItem> items) {
    return items.stream().map(OrderItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public List<OrderItem> getItems() {
    return Collections.unmodifiableList(items);
  }


  public void validate() {
    if (items.isEmpty()) {
      throw new IllegalArgumentException("Pedido deve possuir ao menos 1 item");
    }
    if (total == null || total.signum() < 0) {
      throw new IllegalArgumentException("Total inválido");
    }
  }
}
