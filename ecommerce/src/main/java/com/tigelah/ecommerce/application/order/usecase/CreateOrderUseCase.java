package com.tigelah.ecommerce.application.order.usecase;

import com.tigelah.ecommerce.application.order.command.CreateOrderCmd;
import com.tigelah.ecommerce.domains.order.Order;


public interface CreateOrderUseCase { Order handle(CreateOrderCmd cmd); }
