package com.tigelah.ecommerce.application.reports.queries;

import com.tigelah.ecommerce.application.reports.dto.AvgTicketDTO;

import java.time.LocalDate;
import java.util.List;

public interface AvgTicketByUserQuery {
  List<AvgTicketDTO> execute(LocalDate start, LocalDate end);
}
