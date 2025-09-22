package com.tigelah.ecommerce.application.order.usecase.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tigelah.ecommerce.application.order.command.PayOrderCommand;
import com.tigelah.ecommerce.application.order.ports.OutboxRepository;
import com.tigelah.ecommerce.application.order.ports.OrderRepository;
import com.tigelah.ecommerce.application.order.usecase.PayOrderUseCase;
import com.tigelah.ecommerce.domains.order.Order;
import com.tigelah.ecommerce.domains.order.OrderStatus;
import com.tigelah.ecommerce.domains.order.events.OrderPaidEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PayOrderUseCaseImpl implements PayOrderUseCase {

  private final OrderRepository orderRepository;
  private final OutboxRepository outboxRepository;
  private final ObjectMapper mapper;

  @Override
  @Transactional
  public Order handle(PayOrderCommand cmd) {
    var order = orderRepository.findById(cmd.orderId())
      .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

    if (!order.getUserId().equals(cmd.userId()))
      throw new IllegalStateException("Pedido não pertence ao usuário");

    if (order.getStatus() == OrderStatus.CANCELADO)
      throw new IllegalStateException("Pedido cancelado não pode ser pago");

    if (order.getStatus() == OrderStatus.PAGO)
      return order;

    order.setStatus(OrderStatus.PAGO);
    order.setPaidAt(Instant.now());
    orderRepository.update(order);

    var evt = new OrderPaidEvent(
      order.getId(),
      order.getUserId(),
      order.getItems().stream()
        .map(i -> new OrderPaidEvent.Item(i.productId(), i.quantity()))
        .collect(java.util.stream.Collectors.toList()),
      order.getPaidAt(),
      OrderPaidEvent.type(),
      1
    );

    try {
      var json = mapper.writeValueAsString(evt);
      var rec = new OutboxRepository.OutboxRecord();
      rec.id = java.util.UUID.randomUUID();
      rec.aggregateType = "order";
      rec.aggregateId = order.getId();
      rec.type = OrderPaidEvent.type();
      rec.payload = json;
      rec.status = OutboxRepository.Status.PENDING;
      rec.createdAt = Instant.now();
      outboxRepository.add(rec);
    } catch (Exception e) {
      throw new RuntimeException("Falha ao serializar evento", e);
    }

    return order;
  }
}
