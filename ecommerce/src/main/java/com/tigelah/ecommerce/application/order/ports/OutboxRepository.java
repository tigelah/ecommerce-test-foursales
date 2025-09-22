package com.tigelah.ecommerce.application.order.ports;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository {
  void add(OutboxRecord rec);

  List<OutboxRecord> findNextPending(int limit);

  void markPublished(UUID id);

  void markFailed(UUID id);

  enum Status {PENDING, PUBLISHED, FAILED}

  class OutboxRecord {
    public UUID id;
    public String aggregateType;
    public UUID aggregateId;
    public String type;      // order.paid
    public String payload;   // JSON
    public Status status;
    public Instant createdAt;
    public Instant lastAttempt;
  }
}
