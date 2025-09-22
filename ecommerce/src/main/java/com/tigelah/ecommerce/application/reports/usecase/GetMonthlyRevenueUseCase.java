package com.tigelah.ecommerce.application.reports.usecase;

import com.tigelah.ecommerce.application.reports.dto.MonthlyRevenueDTO;

public interface GetMonthlyRevenueUseCase {
  MonthlyRevenueDTO handle(Integer year, Integer month);
}
