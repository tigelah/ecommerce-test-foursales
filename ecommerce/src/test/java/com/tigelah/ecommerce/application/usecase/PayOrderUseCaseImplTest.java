package com.tigelah.ecommerce.application.usecase;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tigelah.ecommerce.application.order.command.PayOrderCommand;
import com.tigelah.ecommerce.application.order.ports.OutboxRepository;
import com.tigelah.ecommerce.application.order.ports.OrderRepository;
import com.tigelah.ecommerce.application.order.usecase.impl.PayOrderUseCaseImpl;
import com.tigelah.ecommerce.domains.order.Order;
import com.tigelah.ecommerce.domains.order.OrderItem;
import com.tigelah.ecommerce.domains.order.OrderStatus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PayOrderUseCaseImplTest {

  @Test
  void pagaPedidoGeraOutbox() {
    var orderRepo = mock(OrderRepository.class);
    var outbox = mock(OutboxRepository.class);

    var user = UUID.randomUUID();
    var order = Order.builder()
      .id(UUID.randomUUID())
      .userId(user)
      .items(List.of(new OrderItem(UUID.randomUUID(), 1, new BigDecimal("5.00"))))
      .status(OrderStatus.PENDENTE)
      .total(new BigDecimal("5.00"))
      .createdAt(Instant.now())
      .build();

    when(orderRepo.findById(order.getId())).thenReturn(Optional.of(order));
    doAnswer(inv -> inv.getArgument(0)).when(orderRepo).update(any(Order.class));

    var mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    var usecase = new PayOrderUseCaseImpl(orderRepo, outbox, mapper);


    var result = usecase.handle(new PayOrderCommand(order.getId(), user));

    assertEquals(OrderStatus.PAGO, result.getStatus());
    assertNotNull(result.getPaidAt());

    verify(orderRepo, times(1)).update(any(Order.class));

    var captor = ArgumentCaptor.forClass(OutboxRepository.OutboxRecord.class);
    verify(outbox, times(1)).add(captor.capture());

    var rec = captor.getValue();
    assertNotNull(rec);
    assertEquals(order.getId(), rec.aggregateId);
    assertEquals(OutboxRepository.Status.PENDING, rec.status);
    assertTrue(rec.payload.contains(order.getId().toString()));
  }
}
