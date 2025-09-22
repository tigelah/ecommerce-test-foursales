package com.tigelah.ecommerce.application.product.usecase.impl;

import com.tigelah.ecommerce.application.product.command.UpdateProductCmd;
import com.tigelah.ecommerce.application.product.ports.ProductIndexer;
import com.tigelah.ecommerce.application.product.usecase.UpdateProductUseCase;
import com.tigelah.ecommerce.commons.exceptions.NotFoundException;
import com.tigelah.ecommerce.domains.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateProductUseCaseImpl implements UpdateProductUseCase {

  private final ProductRepository repository;
  private final ProductIndexer indexer;

  @Override
  public void handle(UUID id, UpdateProductCmd cmd) {
    var product = repository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado: " + id));

    var updated = product.toBuilder().name(cmd.name()).description(cmd.description()).price(cmd.price()).category(cmd.category()).stock(cmd.stock()).build();
    updated.touchUpdated();
    updated.validate();

    var saved = repository.save(updated);
    indexer.index(saved);
  }
}
