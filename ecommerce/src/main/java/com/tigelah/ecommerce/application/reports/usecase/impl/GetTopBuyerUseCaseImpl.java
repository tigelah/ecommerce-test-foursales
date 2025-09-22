package com.tigelah.ecommerce.application.reports.usecase.impl;

import com.tigelah.ecommerce.application.reports.dto.TopBuyerDTO;
import com.tigelah.ecommerce.application.reports.ports.ReportRepository;
import com.tigelah.ecommerce.application.reports.usecase.GetTopCustomersUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTopCustomersUseCaseImpl implements GetTopCustomersUseCase {
  private final ReportRepository repo;

  @Override
  public List<TopBuyerDTO> handle(Instant start, Instant end, int limit) {
    return repo.topCustomers(start, end, limit <= 0 ? 5 : limit);
  }
}
