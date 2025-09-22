package com.tigelah.ecommerce.application.reports.usecase.impl;

import com.tigelah.ecommerce.application.reports.dto.MonthlyRevenueDTO;
import com.tigelah.ecommerce.application.reports.ports.ReportRepository;
import com.tigelah.ecommerce.application.reports.usecase.GetMonthlyRevenueUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class GetMonthlyRevenueUseCaseImpl implements GetMonthlyRevenueUseCase {
  private final ReportRepository repo;

  @Override
  public MonthlyRevenueDTO handle(Integer year, Integer month) {
    var now = ZonedDateTime.now(ZoneOffset.UTC);
    var ym = (year == null || month == null) ? YearMonth.of(now.getYear(), now.getMonthValue()) : YearMonth.of(year, month);
    BigDecimal total = repo.monthlyRevenue(ym);
    return new MonthlyRevenueDTO(ym.getYear(), ym.getMonthValue(), total != null ? total : BigDecimal.ZERO);
  }
}
