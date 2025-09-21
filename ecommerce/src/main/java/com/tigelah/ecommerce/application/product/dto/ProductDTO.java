package com.tigelah.ecommerce.application.product.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

public record ProductDTO(UUID id, String name, String description, BigDecimal price, String category, BigInteger stock) {}


