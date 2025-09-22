package com.tigelah.ecommerce.application.usecases;

import com.tigelah.ecommerce.application.reports.dto.AvgTicketDTO;
import com.tigelah.ecommerce.application.reports.ports.ReportRepository;
import com.tigelah.ecommerce.application.reports.usecase.impl.GetAvgTicketPerUserUseCaseImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetAvgTicketPerUserUseCaseImplTest {

  @Test
  void calculaTicketMedioPorUsuario() {
    var repo = mock(ReportRepository.class);
    var uc = new GetAvgTicketPerUserUseCaseImpl(repo);

    var u1 = UUID.randomUUID();
    var u2 = UUID.randomUUID();
    var start = Instant.parse("2025-09-01T00:00:00Z");
    var end = Instant.parse("2025-10-01T00:00:00Z");

    var expected = List.of(
      new AvgTicketDTO(u1, new BigDecimal("200.00")),
      new AvgTicketDTO(u2, new BigDecimal("150.50"))
    );
    when(repo.avgTicketPerUser(start, end)).thenReturn(expected);

    var out = uc.handle(start, end);

    assertEquals(expected, out);
    verify(repo).avgTicketPerUser(start, end);
  }
}
