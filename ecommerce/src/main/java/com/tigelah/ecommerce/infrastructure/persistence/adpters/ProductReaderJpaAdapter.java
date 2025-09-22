package com.tigelah.ecommerce.infrastructure.persistence.adpters;

import com.tigelah.ecommerce.application.order.ports.ProductReader;
import com.tigelah.ecommerce.infrastructure.persistence.jpa.repositories.SpringDataProductJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductReaderJpaAdapter implements ProductReader {

  private final SpringDataProductJpa jpa; // sua interface de produtos já existente

  @Override
  public Optional<ProductReader.ProductSnapshot> getById(UUID id) {
    return jpa.findById(id).map(e -> new ProductSnapshot(e.getId(), e.getName(), e.getPrice(), e.getStock().intValue()));
  }
}
