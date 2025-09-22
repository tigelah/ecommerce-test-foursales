package com.tigelah.ecommerce.application.product.usecase;

import java.util.UUID;

public interface DeleteProductUseCase {
  void handle(UUID id);
}
