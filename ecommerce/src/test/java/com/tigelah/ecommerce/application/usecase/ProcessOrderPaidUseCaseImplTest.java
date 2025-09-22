package com.tigelah.ecommerce.application.usecase;

import com.tigelah.ecommerce.application.inventory.ports.InventoryRepository;
import com.tigelah.ecommerce.application.inventory.usecase.impl.ProcessOrderPaidUseCaseImpl;
import com.tigelah.ecommerce.domains.order.events.OrderPaidEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessOrderPaidUseCaseImplTest {

  @Test
  void baixaEstoque_eMarcaProcessado() {
    var repo = mock(InventoryRepository.class);

    var p1 = UUID.randomUUID();
    var evt = new OrderPaidEvent(
      UUID.randomUUID(),
      UUID.randomUUID(),
      List.of(new OrderPaidEvent.Item(p1, 2)),
      Instant.now(),
      "order.paid",
      1
    );

    when(repo.wasProcessed(evt.orderId())).thenReturn(false);
    when(repo.findProductForUpdate(p1)).thenReturn(Optional.of(
      new InventoryRepository.ProductSnapshot(p1, "X", new BigDecimal("10.00"), new BigInteger("5"))
    ));

    var uc = new ProcessOrderPaidUseCaseImpl(repo);
    uc.handle(evt);

    verify(repo, times(1)).saveProduct(argThat(ps ->
      ps.id().equals(p1) && ps.stock().equals(new BigInteger("3"))
    ));
    verify(repo, times(1)).markProcessed(evt.orderId(), "order.paid");
  }

  @Test
  void idempotente_naoBaixaSeJaProcessado() {
    var repo = mock(InventoryRepository.class);
    var p1 = UUID.randomUUID();

    var evt = new OrderPaidEvent(
      UUID.randomUUID(),
      UUID.randomUUID(),
      List.of(new OrderPaidEvent.Item(p1, 1)),
      Instant.now(),
      "order.paid",
      1
    );

    when(repo.wasProcessed(evt.orderId())).thenReturn(true);

    var uc = new ProcessOrderPaidUseCaseImpl(repo);
    uc.handle(evt);

    verify(repo, never()).findProductForUpdate(any());
    verify(repo, never()).saveProduct(any());
    verify(repo, never()).markProcessed(any(), any());
  }

  @Test
  void falhaQuandoSemEstoque() {
    var repo = mock(InventoryRepository.class);
    var p1 = UUID.randomUUID();
    var evt = new OrderPaidEvent(
      UUID.randomUUID(), UUID.randomUUID(),
      List.of(new OrderPaidEvent.Item(p1, 10)),
      Instant.now(), "order.paid", 1
    );

    when(repo.wasProcessed(evt.orderId())).thenReturn(false);
    when(repo.findProductForUpdate(p1)).thenReturn(Optional.of(
      new InventoryRepository.ProductSnapshot(p1, "X", new BigDecimal("10.00"), new BigInteger("3"))
    ));

    var uc = new ProcessOrderPaidUseCaseImpl(repo);
    assertThrows(IllegalStateException.class, () -> uc.handle(evt));
  }
}
