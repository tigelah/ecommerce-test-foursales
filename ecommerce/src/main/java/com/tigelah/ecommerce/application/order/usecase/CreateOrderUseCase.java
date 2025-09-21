package com.tigelah.ecommerce.application.order.command;

import java.util.UUID;

public interface CreateOrderUseCase { UUID handle(CreateOrderCmd cmd, UUID userId); }
