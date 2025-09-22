package com.tigelah.ecommerce.application.order.dto;

import java.util.UUID;

public record PayOrderCommand(UUID orderId, UUID userId) {}
