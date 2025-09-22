package com.tigelah.ecommerce.application.reports.usecase;

import com.tigelah.ecommerce.application.reports.dto.TopBuyerDTO;

import java.time.Instant;
import java.util.List;

public interface GetTopCustomersUseCase {
  List<TopBuyerDTO> handle(Instant start, Instant end, int limit);
}
