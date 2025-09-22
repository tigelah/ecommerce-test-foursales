package com.tigelah.ecommerce.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.tigelah.ecommerce.infrastructure.persistence.jpa.entities.OrderEntity;

public interface ReportJpa extends JpaRepository<OrderEntity, UUID> {

  interface TopBuyersRow {
    String getUserId();
    BigDecimal getTotalSpent();
    Long getOrdersCount();
  }

  interface AvgTicketRow {
    String getUserId();
    BigDecimal getAvgTicket();
  }

  // Top clientes por valor total no período (status PAGO)
  @Query(value = """
      SELECT o.user_id AS userId,
             SUM(o.total) AS totalSpent,
             COUNT(*) AS ordersCount
      FROM orders o
      WHERE o.status = 'PAGO'
        AND (:start IS NULL OR o.paid_at >= :start)
        AND (:end   IS NULL OR o.paid_at <  :end)
      GROUP BY o.user_id
      ORDER BY totalSpent DESC
      LIMIT :limit
      """, nativeQuery = true)
  List<TopBuyersRow> topBuyers(Instant start, Instant end, int limit);

  // Ticket médio por usuário no período (status PAGO)
  @Query(value = """
      SELECT o.user_id AS userId,
             AVG(o.total) AS avgTicket
      FROM orders o
      WHERE o.status = 'PAGO'
        AND (:start IS NULL OR o.paid_at >= :start)
        AND (:end   IS NULL OR o.paid_at <  :end)
      GROUP BY o.user_id
      ORDER BY avgTicket DESC
      """, nativeQuery = true)
  List<AvgTicketRow> avgTicketPerUser(Instant start, Instant end);

  // Faturamento do mês (status PAGO) — usamos o primeiro/último dia via parâmetros
  @Query(value = """
      SELECT COALESCE(SUM(o.total), 0)
      FROM orders o
      WHERE o.status = 'PAGO'
        AND o.paid_at >= :startMonth
        AND o.paid_at <  :endMonth
      """, nativeQuery = true)
  BigDecimal monthlyRevenue(Instant startMonth, Instant endMonth);
}
