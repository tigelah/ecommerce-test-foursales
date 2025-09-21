package com.tigelah.ecommerce.application.product.command;

import java.math.BigDecimal;
import java.math.BigInteger;

public record UpdateProductCmd(String name, String description, BigDecimal price, String category, BigInteger stock) {}

