package com.tigelah.ecommerce.application.product.usecase.impl;

import com.tigelah.ecommerce.application.product.dto.ProductDTO;
import com.tigelah.ecommerce.application.product.ports.ProductSearchGateway;
import com.tigelah.ecommerce.application.product.query.ProductSearchQuery;
import com.tigelah.ecommerce.application.product.usecase.SearchProductsUseCase;
import com.tigelah.ecommerce.commons.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchProductsUseCaseImpl implements SearchProductsUseCase {

  private final ProductSearchGateway gateway;

  @Override
  public PageResult<ProductDTO> handle(ProductSearchQuery q) {
    if (q.minPrice() != null && q.maxPrice() != null && q.minPrice().compareTo(q.maxPrice()) > 0) {
      throw new IllegalArgumentException("Faixa de preço inválida: minPrice > maxPrice");
    }
    return gateway.search(q);
  }
}
