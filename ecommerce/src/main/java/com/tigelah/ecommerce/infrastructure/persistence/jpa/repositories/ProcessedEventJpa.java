package com.tigelah.ecommerce.infrastructure.persistence.jpa.repositories;

import com.tigelah.ecommerce.infrastructure.persistence.jpa.entities.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessedEventJpa extends JpaRepository<ProcessedEventEntity, UUID> {
}
