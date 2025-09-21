package com.tigelah.ecommerce.application.report;

import java.math.BigDecimal;
import java.util.UUID;

public record TopBuyerDTO(UUID userId, BigDecimal total) {}
