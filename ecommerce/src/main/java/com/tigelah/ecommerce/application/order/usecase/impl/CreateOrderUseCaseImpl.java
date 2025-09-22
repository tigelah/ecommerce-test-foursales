package com.tigelah.ecommerce.application.order.usecase.impl;

import com.tigelah.ecommerce.application.order.command.CreateOrderCmd;
import com.tigelah.ecommerce.application.order.ports.OrderRepository;
import com.tigelah.ecommerce.application.order.ports.ProductReader;
import com.tigelah.ecommerce.application.order.usecase.CreateOrderUseCase;
import com.tigelah.ecommerce.domains.order.Order;
import com.tigelah.ecommerce.domains.order.OrderItem;
import com.tigelah.ecommerce.domains.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

  private final ProductReader productReader;
  private final OrderRepository orderRepository;

  @Override
  @Transactional
  public Order handle(CreateOrderCmd cmd) {
    var items = new ArrayList<OrderItem>();
    boolean hasInsufficient = false;

    BigDecimal total = BigDecimal.ZERO;

    for (var it : cmd.items()) {
      var snapshot = productReader.getById(it.productId()).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + it.productId()));
      if (snapshot.stock() < it.quantity()) {
        hasInsufficient = true;
      }
      var priceNow = snapshot.price();
      total = total.add(priceNow.multiply(BigDecimal.valueOf(it.quantity())));
      items.add(new OrderItem(it.productId(), it.quantity(), priceNow));
    }

    var order = Order.builder().userId(cmd.userId()).items(items).status(hasInsufficient ? OrderStatus.CANCELADO : OrderStatus.PENDENTE).total(total).createdAt(Instant.now()).build();

    return orderRepository.save(order);
  }
}
