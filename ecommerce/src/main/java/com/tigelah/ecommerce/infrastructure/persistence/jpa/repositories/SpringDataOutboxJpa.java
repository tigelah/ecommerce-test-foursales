package com.tigelah.ecommerce.infrastructure.persistence.jpa.repositories;

import com.tigelah.ecommerce.infrastructure.persistence.jpa.entities.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataOutboxJpa extends JpaRepository<OutboxEntity, UUID> {
  List<OutboxEntity> findTop50ByStatusOrderByCreatedAtAsc(String status);
}
