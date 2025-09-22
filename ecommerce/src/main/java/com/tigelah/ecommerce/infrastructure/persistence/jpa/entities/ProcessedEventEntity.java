package com.tigelah.ecommerce.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "processed_events")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProcessedEventEntity {
  @Id
  private UUID id;             // orderId (chave do evento)
  private String type;         // "order.paid"
  private Instant processedAt;
}
