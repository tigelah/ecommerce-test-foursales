package com.tigelah.ecommerce.application.reports.dto;

import java.math.BigDecimal;

public record MonthlyRevenueDTO(int year, int month, BigDecimal totalRevenue) {
}
