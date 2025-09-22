package com.tigelah.ecommerce.application.usecase;

import com.tigelah.ecommerce.application.order.command.CreateOrderCmd;
import com.tigelah.ecommerce.application.order.ports.OrderRepository;
import com.tigelah.ecommerce.application.order.ports.ProductReader;
import com.tigelah.ecommerce.application.order.usecase.impl.CreateOrderUseCaseImpl;
import com.tigelah.ecommerce.domains.order.Order;
import com.tigelah.ecommerce.domains.order.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateOrderUseCaseImplTest {

  @Test
  void criaPedidoPendenteCalculandoTotal() {
    var productReader = mock(ProductReader.class);
    var orderRepo = mock(OrderRepository.class);

    var p1 = UUID.randomUUID();
    when(productReader.getById(p1)).thenReturn(Optional.of(
      new ProductReader.ProductSnapshot(p1, "X", new BigDecimal("10.00"), 5)
    ));

    when(orderRepo.save(any(Order.class))).thenAnswer(inv ->
      inv.getArgument(0, Order.class)
    );

    var usecase = new CreateOrderUseCaseImpl(productReader, orderRepo);

    var user = UUID.randomUUID();
    var cmd = new CreateOrderCmd(user, List.of(new CreateOrderCmd.Item(p1, 2)));

    var order = usecase.handle(cmd);

    assertEquals(OrderStatus.PENDENTE, order.getStatus());
    assertEquals(new BigDecimal("20.00"), order.getTotal());
  }

  @Test
  void cancelaQuandoSemEstoque() {
    var productReader = mock(ProductReader.class);
    var orderRepo = mock(OrderRepository.class);

    var p1 = UUID.randomUUID();
    when(productReader.getById(p1)).thenReturn(Optional.of(
      new ProductReader.ProductSnapshot(p1, "X", new BigDecimal("10.00"), 1)
    ));
    when(orderRepo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

    var usecase = new CreateOrderUseCaseImpl(productReader, orderRepo);
    var order = usecase.handle(new CreateOrderCmd(UUID.randomUUID(),
      List.of(new CreateOrderCmd.Item(p1, 3))));

    assertEquals(OrderStatus.CANCELADO, order.getStatus());
  }
}
