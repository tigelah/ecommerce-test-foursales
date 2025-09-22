package com.tigelah.ecommerce.application.product.ports;

import com.tigelah.ecommerce.domains.product.Product;

import java.util.UUID;

public interface ProductIndexer {
  void index(Product product);

  void delete(UUID id);
}
