package com.tigelah.ecommerce.application.product.mapper;

import com.tigelah.ecommerce.application.product.dto.ProductDTO;
import com.tigelah.ecommerce.domains.product.Product;

public final class ProductMapper {
  private ProductMapper() {
  }

  public static ProductDTO toDTO(Product p) {
    return new ProductDTO(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getCategory(), p.getStock());
  }
}
