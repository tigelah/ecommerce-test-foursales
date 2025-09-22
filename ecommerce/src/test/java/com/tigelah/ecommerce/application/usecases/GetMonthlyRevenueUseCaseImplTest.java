package com.tigelah.ecommerce.application.usecases;

import com.tigelah.ecommerce.application.reports.dto.MonthlyRevenueDTO;
import com.tigelah.ecommerce.application.reports.ports.ReportRepository;
import com.tigelah.ecommerce.application.reports.usecase.impl.GetMonthlyRevenueUseCaseImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetMonthlyRevenueUseCaseImplTest {

  @Test
  void calculaFaturamentoDoMesInformado() {
    var repo = mock(ReportRepository.class);
    var uc = new GetMonthlyRevenueUseCaseImpl(repo);

    when(repo.monthlyRevenue(YearMonth.of(2025, 9))).thenReturn(new BigDecimal("1234.56"));

    MonthlyRevenueDTO out = uc.handle(2025, 9);

    assertEquals(2025, out.year());
    assertEquals(9, out.month());
    assertEquals(new BigDecimal("1234.56"), out.totalRevenue());
    verify(repo).monthlyRevenue(YearMonth.of(2025, 9));
  }
}
