package com.tigelah.ecommerce.entrypoints.http;

import com.tigelah.ecommerce.application.reports.dto.AvgTicketDTO;
import com.tigelah.ecommerce.application.reports.dto.MonthlyRevenueDTO;
import com.tigelah.ecommerce.application.reports.dto.TopBuyerDTO;
import com.tigelah.ecommerce.application.reports.usecase.GetAvgTicketPerUserUseCase;
import com.tigelah.ecommerce.application.reports.usecase.GetMonthlyRevenueUseCase;
import com.tigelah.ecommerce.application.reports.usecase.GetTopBuyersUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReportsController {

  private final GetTopBuyersUseCase getTopBuyers;
  private final GetAvgTicketPerUserUseCase getAvgTicket;
  private final GetMonthlyRevenueUseCase getMonthly;

  @GetMapping("/top-buyers")
  public List<TopBuyerDTO> topBuyers(
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end,
    @RequestParam(defaultValue = "5") int limit
  ) {
    return getTopBuyers.handle(start, end, limit);
  }

  @GetMapping("/avg-ticket")
  public List<AvgTicketDTO> avgTicket(
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end
  ) {
    return getAvgTicket.handle(start, end);
  }

  @GetMapping("/monthly-revenue")
  public MonthlyRevenueDTO monthlyRevenue(
    @RequestParam(required = false) Integer year,
    @RequestParam(required = false) Integer month
  ) {
    return getMonthly.handle(year, month);
  }
}
