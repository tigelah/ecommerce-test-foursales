package com.tigelah.ecommerce.application.product.usecase;

import com.tigelah.ecommerce.application.product.command.UpdateProductCmd;

import java.util.UUID;

public interface UpdateProductUseCase {
  void handle(UUID id, UpdateProductCmd cmd);
}
