package com.tigelah.ecommerce.application.order.usecase;

import com.tigelah.ecommerce.application.order.command.CreateOrderCmd;

import java.util.UUID;

public interface CreateOrderUseCase { UUID handle(CreateOrderCmd cmd, UUID userId); }
