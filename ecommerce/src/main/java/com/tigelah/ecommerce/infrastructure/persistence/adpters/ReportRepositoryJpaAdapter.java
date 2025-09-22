package com.tigelah.ecommerce.infrastructure.persistence.adpters;

import com.tigelah.ecommerce.application.reports.dto.AvgTicketDTO;
import com.tigelah.ecommerce.application.reports.dto.TopBuyerDTO;
import com.tigelah.ecommerce.application.reports.ports.ReportRepository;
import com.tigelah.ecommerce.infrastructure.persistence.jpa.repositories.ReportJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.UUID;

@Repository
@Primary
@RequiredArgsConstructor
public class ReportRepositoryJpaAdapter implements ReportRepository {

  private final ReportJpa jpa;

  @Override
  public List<TopBuyerDTO> topBuyers(java.time.Instant start, java.time.Instant end, int limit) {
    return jpa.topBuyers(start, end, limit).stream()
      .map(r -> new TopBuyerDTO( UUID.fromString(r.getUserId()), r.getTotalSpent(), r.getOrdersCount()))
      .toList();
  }

  @Override
  public List<AvgTicketDTO> avgTicketPerUser(java.time.Instant start, java.time.Instant end) {
    return jpa.avgTicketPerUser(start, end).stream()
      .map(r -> new AvgTicketDTO(UUID.fromString(r.getUserId()), r.getAvgTicket()))
      .toList();
  }

  @Override
  public BigDecimal monthlyRevenue(YearMonth ym) {
    var start = ym.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
    var end = ym.plusMonths(1).atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
    return jpa.monthlyRevenue(start, end);
  }
}
