package com.tigelah.ecommerce.infrastructure.persistence.adpters;

import com.tigelah.ecommerce.application.inventory.ports.InventoryRepository;
import com.tigelah.ecommerce.infrastructure.persistence.jpa.repositories.SpringDataProductJpa;
import com.tigelah.ecommerce.infrastructure.persistence.jpa.repositories.ProcessedEventJpa;
import com.tigelah.ecommerce.infrastructure.persistence.jpa.entities.ProcessedEventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
@RequiredArgsConstructor
public class InventoryRepositoryJpaAdapter implements InventoryRepository {

  private final SpringDataProductJpa products;
  private final ProcessedEventJpa processed;

  @Override
  public Optional<ProductSnapshot> findProductForUpdate(UUID id) {
    return products.findOneForUpdate(id)
      .map(e -> new ProductSnapshot(e.getId(), e.getName(), e.getPrice(), e.getStock()));
  }

  @Override
  public void saveProduct(ProductSnapshot p) {
    var e = products.findById(p.id()).orElseThrow();
    e.setStock(p.stock());
    products.save(e);
  }

  @Override
  public boolean wasProcessed(UUID orderId) {
    return processed.existsById(orderId);
  }

  @Override
  public void markProcessed(UUID orderId, String type) {
    processed.save(ProcessedEventEntity.builder()
      .id(orderId)
      .type(type)
      .processedAt(Instant.now())
      .build());
  }
}

