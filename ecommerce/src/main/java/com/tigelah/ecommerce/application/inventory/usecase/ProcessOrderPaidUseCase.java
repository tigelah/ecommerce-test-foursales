package com.tigelah.ecommerce.application.inventory.usecase;

import com.tigelah.ecommerce.domains.order.events.OrderPaidEvent;

public interface ProcessOrderPaidUseCase {
  void handle(OrderPaidEvent event);
}
