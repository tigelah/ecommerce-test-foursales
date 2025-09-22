package com.tigelah.ecommerce.application.report.queries;

import com.tigelah.ecommerce.application.report.dto.AvgTicketDTO;

import java.time.LocalDate;
import java.util.List;

public interface AvgTicketByUserQuery {
  List<AvgTicketDTO> execute(LocalDate start, LocalDate end);
}
