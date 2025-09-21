package com.tigelah.ecommerce.application.order.usecase;

import java.util.UUID;

public interface PayOrderUseCase { void handle(UUID orderId, UUID userId); }
