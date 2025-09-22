package com.tigelah.ecommerce.application.product.usecase.impl;

import com.tigelah.ecommerce.application.product.command.CreateProductCmd;
import com.tigelah.ecommerce.application.product.ports.ProductIndexer;
import com.tigelah.ecommerce.application.product.usecase.CreateProductUseCase;
import com.tigelah.ecommerce.domains.product.Product;
import com.tigelah.ecommerce.domains.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateProductUseCaseImpl implements CreateProductUseCase {

  private final ProductRepository repository;
  private final ProductIndexer indexer;

  @Override
  public UUID handle(CreateProductCmd cmd) {
    var product = Product.builder().name(cmd.name()).description(cmd.description()).price(cmd.price()).category(cmd.category()).stock(cmd.stock() == null ? BigInteger.ZERO : cmd.stock()).build();
    product.validate();

    var saved = repository.save(product);
    indexer.index(saved);
    return saved.getId();
  }
}
