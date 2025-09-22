package com.tigelah.ecommerce.application.reports.usecase;

import com.tigelah.ecommerce.application.reports.dto.AvgTicketDTO;

import java.time.Instant;
import java.util.List;

public interface GetAvgTicketPerUserUseCase {
  List<AvgTicketDTO> handle(Instant start, Instant end);
}
