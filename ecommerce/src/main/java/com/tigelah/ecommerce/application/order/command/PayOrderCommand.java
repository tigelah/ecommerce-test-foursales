package com.tigelah.ecommerce.application.order.command;

import java.util.UUID;

public record PayOrderCommand(UUID orderId, UUID userId) {}
