package com.tigelah.ecommerce.application.reports.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AvgTicketDTO(UUID userId, BigDecimal avgTicket) {}
