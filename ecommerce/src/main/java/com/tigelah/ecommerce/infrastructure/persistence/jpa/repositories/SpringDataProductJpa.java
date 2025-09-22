package com.tigelah.ecommerce.infrastructure.persistence.jpa.repositories;

import com.tigelah.ecommerce.infrastructure.persistence.jpa.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, UUID> { }

