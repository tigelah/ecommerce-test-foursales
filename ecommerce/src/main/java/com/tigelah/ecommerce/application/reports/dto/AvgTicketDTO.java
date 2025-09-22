package com.tigelah.ecommerce.application.report.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AvgTicketDTO(UUID userId, BigDecimal avgTicket) {}
