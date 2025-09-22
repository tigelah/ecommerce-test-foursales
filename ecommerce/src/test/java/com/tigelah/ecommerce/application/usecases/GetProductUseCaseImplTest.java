package com.tigelah.ecommerce.application.usecases;

import com.tigelah.ecommerce.application.product.usecase.impl.GetProductUseCaseImpl;
import com.tigelah.ecommerce.domains.product.Product;
import com.tigelah.ecommerce.domains.product.ProductRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetProductUseCaseImplTest {

  @Test
  void deve_retornar_dto() {
    var repo = mock(ProductRepository.class);
    var id = UUID.randomUUID();

    var p = Product.builder()
      .id(id).name("N").description("D").price(new BigDecimal("3.40"))
      .category("C").stock(BigInteger.TEN)
      .createdAt(Instant.now()).updatedAt(Instant.now())
      .build();

    when(repo.findById(id)).thenReturn(Optional.of(p));

    var uc = new GetProductUseCaseImpl(repo);
    var dto = uc.handle(id);

    assertEquals(id, dto.id());
    assertEquals("N", dto.name());
  }
}
