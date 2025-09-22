package com.tigelah.ecommerce.application.product.usecase;

import com.tigelah.ecommerce.application.product.command.CreateProductCmd;

import java.util.UUID;

public interface CreateProductUseCase {
  UUID handle(CreateProductCmd cmd);
}
