package com.tigelah.ecommerce.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="order_outbox")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEntity {
  @Id
  private UUID id;
  private String aggregateType;
  private UUID aggregateId;
  private String type;
  @Column(columnDefinition = "TEXT")
  private String payload;
  private String status;
  private Instant createdAt;
  private Instant lastAttempt;

}
