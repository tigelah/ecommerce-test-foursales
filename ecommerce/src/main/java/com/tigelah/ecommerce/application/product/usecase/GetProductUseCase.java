package com.tigelah.ecommerce.application.product.usecase;

import com.tigelah.ecommerce.application.product.dto.ProductDTO;

import java.util.UUID;

public interface GetProductUseCase { ProductDTO handle(UUID id); }
