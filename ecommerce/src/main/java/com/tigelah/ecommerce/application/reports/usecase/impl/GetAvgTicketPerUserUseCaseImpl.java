package com.tigelah.ecommerce.application.reports.usecase.impl;

import com.tigelah.ecommerce.application.reports.dto.AvgTicketDTO;
import com.tigelah.ecommerce.application.reports.ports.ReportRepository;
import com.tigelah.ecommerce.application.reports.usecase.GetAvgTicketPerUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAvgTicketPerUserUseCaseImpl implements GetAvgTicketPerUserUseCase {
  private final ReportRepository repo;

  @Override
  public List<AvgTicketDTO> handle(Instant start, Instant end) {
    return repo.avgTicketPerUser(start, end);
  }
}
