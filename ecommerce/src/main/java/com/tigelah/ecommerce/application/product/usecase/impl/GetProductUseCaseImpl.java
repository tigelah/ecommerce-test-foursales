package com.tigelah.ecommerce.application.product.usecase.impl;

import com.tigelah.ecommerce.application.product.dto.ProductDTO;
import com.tigelah.ecommerce.application.product.mapper.ProductMapper;
import com.tigelah.ecommerce.application.product.usecase.GetProductUseCase;
import com.tigelah.ecommerce.commons.exceptions.NotFoundException;
import com.tigelah.ecommerce.domains.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetProductUseCaseImpl implements GetProductUseCase {

  private final ProductRepository repository;

  @Override
  public ProductDTO handle(UUID id) {
    return repository.findById(id).map(ProductMapper::toDTO).orElseThrow(() -> new NotFoundException("Produto não encontrado: " + id));
  }
}
