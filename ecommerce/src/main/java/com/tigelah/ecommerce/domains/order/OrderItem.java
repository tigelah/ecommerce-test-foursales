package com.tigelah.ecommerce.domains.order;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItem(UUID productId, int quantity, BigDecimal unitPrice) {
  public BigDecimal subtotal(){ return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
}
