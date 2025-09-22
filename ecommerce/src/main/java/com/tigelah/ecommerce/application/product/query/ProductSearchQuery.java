package com.tigelah.ecommerce.application.product.query;

import java.math.BigDecimal;

public record ProductSearchQuery(
  String name,
  String category,
  BigDecimal minPrice,
  BigDecimal maxPrice,
  int page,
  int size
) {
  public ProductSearchQuery {
    if (page < 0) page = 0;
    if (size <= 0 || size > 100) size = 20;
  }
}
