package com.tigelah.ecommerce.application.product.usecase.impl;

import com.tigelah.ecommerce.application.product.ports.ProductIndexer;
import com.tigelah.ecommerce.application.product.usecase.DeleteProductUseCase;
import com.tigelah.ecommerce.domains.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteProductUseCaseImpl implements DeleteProductUseCase {

  private final ProductRepository repository;
  private final ProductIndexer indexer;

  @Override
  public void handle(UUID id) {
    repository.deleteById(id);
    indexer.delete(id);
  }
}
