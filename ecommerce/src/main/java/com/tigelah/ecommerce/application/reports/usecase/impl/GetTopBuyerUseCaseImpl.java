package com.tigelah.ecommerce.application.reports.usecase.impl;

import com.tigelah.ecommerce.application.reports.dto.TopBuyerDTO;
import com.tigelah.ecommerce.application.reports.ports.ReportRepository;
import com.tigelah.ecommerce.application.reports.usecase.GetTopBuyersUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTopBuyerUseCaseImpl implements GetTopBuyersUseCase {
  private final ReportRepository repo;

  @Override
  public List<TopBuyerDTO> handle(Instant start, Instant end, int limit) {
    return repo.topBuyers(start, end, limit <= 0 ? 5 : limit);
  }
}
