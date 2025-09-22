package com.tigelah.ecommerce.application.reports.ports;

import com.tigelah.ecommerce.application.reports.dto.AvgTicketDTO;
import com.tigelah.ecommerce.application.reports.dto.TopBuyerDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

public interface ReportRepository {
  List<TopBuyerDTO> topBuyers(Instant startInclusive, Instant endExclusive, int limit);

  List<AvgTicketDTO> avgTicketPerUser(Instant startInclusive, Instant endExclusive);

  BigDecimal monthlyRevenue(YearMonth ym);
}
