package com.tigelah.ecommerce.infrastructure.persistence.adpters;

import com.tigelah.ecommerce.application.order.ports.OutboxRepository;
import com.tigelah.ecommerce.infrastructure.persistence.jpa.entities.OutboxEntity;
import com.tigelah.ecommerce.infrastructure.persistence.jpa.repositories.SpringDataOutboxJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
@Primary
@RequiredArgsConstructor
public class OutboxRepositoryJpaAdapter implements OutboxRepository {

  private final SpringDataOutboxJpa jpa;

  @Override
  public void add(OutboxRecord rec) {
    var e = OutboxEntity.builder().id(rec.id).aggregateType(rec.aggregateType).aggregateId(rec.aggregateId).type(rec.type).payload(rec.payload).status(rec.status.name()).createdAt(rec.createdAt).lastAttempt(rec.lastAttempt).build();
    jpa.save(e);
  }

  @Override
  public List<OutboxRecord> findNextPending(int limit) {
    return jpa.findTop50ByStatusOrderByCreatedAtAsc(Status.PENDING.name()).stream().map(this::toRecord).limit(limit).toList();
  }

  @Override
  public void markPublished(UUID id) {
    jpa.findById(id).ifPresent(e -> {
      e.setStatus(Status.PUBLISHED.name());
      jpa.save(e);
    });
  }

  @Override
  public void markFailed(UUID id) {
    jpa.findById(id).ifPresent(e -> {
      e.setStatus(Status.FAILED.name());
      e.setLastAttempt(Instant.now());
      jpa.save(e);
    });
  }

  private OutboxRecord toRecord(OutboxEntity e) {
    var r = new OutboxRecord();
    r.id = e.getId();
    r.aggregateType = e.getAggregateType();
    r.aggregateId = e.getAggregateId();
    r.type = e.getType();
    r.payload = e.getPayload();
    r.status = Status.valueOf(e.getStatus());
    r.createdAt = e.getCreatedAt();
    r.lastAttempt = e.getLastAttempt();
    return r;
  }
}
