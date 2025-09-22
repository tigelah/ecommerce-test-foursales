package com.tigelah.ecommerce.application.usecases;

import com.tigelah.ecommerce.application.reports.dto.TopBuyerDTO;
import com.tigelah.ecommerce.application.reports.ports.ReportRepository;
import com.tigelah.ecommerce.application.reports.usecase.impl.GetTopBuyerUseCaseImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetTopCustomersUseCaseImplTest {

  @Test
  void retornaTop5ComFiltroDatas() {
    var repo = mock(ReportRepository.class);
    var uc = new GetTopBuyerUseCaseImpl(repo);

    var u1 = UUID.randomUUID();
    var u2 = UUID.randomUUID();
    var start = Instant.parse("2025-09-01T00:00:00Z");
    var end = Instant.parse("2025-10-01T00:00:00Z");

    var expected = List.of(
      new TopBuyerDTO(u1, new BigDecimal("1000.00"), 5),
      new TopBuyerDTO(u2, new BigDecimal("500.00"), 3)
    );
    when(repo.topBuyers(start, end, 5)).thenReturn(expected);

    var out = uc.handle(start, end, 5);

    assertEquals(expected, out);
    verify(repo).topBuyers(start, end, 5);
  }
}

