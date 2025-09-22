package com.tigelah.ecommerce.infrastructure.persistence.jpa.repository;

import com.tigelah.ecommerce.infrastructure.persistence.jpa.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataOrderJpa extends JpaRepository<OrderEntity, UUID> {}
