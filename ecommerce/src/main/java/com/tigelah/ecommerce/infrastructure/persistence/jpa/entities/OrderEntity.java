package com.tigelah.ecommerce.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Entity @Table(name="orders")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderEntity {
  @Id private UUID id;
  private UUID userId;
  private String status;
  private BigDecimal total;
  private Instant createdAt;
  private Instant paidAt;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<OrderItemEntity> items = new ArrayList<>();
}
