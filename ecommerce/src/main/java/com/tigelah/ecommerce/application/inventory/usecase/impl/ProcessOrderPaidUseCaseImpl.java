package com.tigelah.ecommerce.application.inventory.usecase.impl;

import com.tigelah.ecommerce.application.inventory.ports.InventoryRepository;
import com.tigelah.ecommerce.application.inventory.usecase.ProcessOrderPaidUseCase;
import com.tigelah.ecommerce.domains.order.events.OrderPaidEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class ProcessOrderPaidUseCaseImpl implements ProcessOrderPaidUseCase {

  private final InventoryRepository repo;

  @Override
  @Transactional
  public void handle(OrderPaidEvent event) {

    if (repo.wasProcessed(event.orderId())) return;


    for (var it : event.items()) {
      var ps = repo.findProductForUpdate(it.productId()).orElseThrow(() -> new IllegalStateException("Produto não encontrado: " + it.productId()));

      var current = ps.stock() == null ? BigInteger.ZERO : ps.stock();
      var qty = BigInteger.valueOf(it.quantity());
      if (current.compareTo(qty) < 0) {
        throw new IllegalStateException("Estoque insuficiente para produto " + it.productId());
      }
      var updated = current.subtract(qty);
      repo.saveProduct(new InventoryRepository.ProductSnapshot(ps.id(), ps.name(), ps.price(), updated));
    }

    repo.markProcessed(event.orderId(), event.eventType());
  }
}
