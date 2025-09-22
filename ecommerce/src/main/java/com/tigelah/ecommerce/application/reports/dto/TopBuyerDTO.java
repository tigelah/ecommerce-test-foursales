package com.tigelah.ecommerce.application.reports.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TopBuyerDTO(UUID userId, BigDecimal totalSpent, long ordersCount) {}
