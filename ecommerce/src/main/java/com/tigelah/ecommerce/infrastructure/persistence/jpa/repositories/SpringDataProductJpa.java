package com.tigelah.ecommerce.infrastructure.persistence.jpa.repositories;

import com.tigelah.ecommerce.infrastructure.persistence.jpa.entities.ProductEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataProductJpa extends JpaRepository<ProductEntity, UUID> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select p from ProductEntity p where p.id = :id")
  Optional<ProductEntity> findOneForUpdate(@Param("id") UUID id);
}

