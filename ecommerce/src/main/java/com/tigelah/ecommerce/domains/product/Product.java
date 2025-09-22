package com.tigelah.ecommerce.domains.product;


import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

  @Builder.Default
  private final UUID id = UUID.randomUUID();
  @Builder.Default
  private final Instant createdAt = Instant.now();
  @NonNull
  private String name;
  private String description;
  @NonNull
  private BigDecimal price;
  private String category;
  @NonNull
  private BigInteger stock;
  @Builder.Default
  private Instant updatedAt = Instant.now();

  public void touchUpdated() {
    this.updatedAt = Instant.now();
  }

  public void validate() {
    if (price.signum() < 0) {
      throw new IllegalArgumentException("Preço não pode ser negativo");
    }
    if (stock.signum() < 0) {
      throw new IllegalArgumentException("Estoque não pode ser negativo");
    }
  }

}
