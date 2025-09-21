package com.tigelah.ecommerce.application.usecase;

import com.tigelah.ecommerce.application.product.command.UpdateProductCmd;
import com.tigelah.ecommerce.application.product.ports.ProductIndexer;
import com.tigelah.ecommerce.application.product.usecase.impl.UpdateProductUseCaseImpl;
import com.tigelah.ecommerce.domains.product.Product;
import com.tigelah.ecommerce.domains.product.ProductRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateProductUseCaseImplTest {

  @Test
  void deve_atualizar_e_reindexar() {
    var repo = mock(ProductRepository.class);
    var indexer = mock(ProductIndexer.class);
    var id = UUID.randomUUID();

    var existing = Product.builder()
      .id(id)
      .name("Antigo")
      .description("D")
      .price(new BigDecimal("1.00"))
      .category("C")
      .stock(BigInteger.ONE)
      .createdAt(Instant.now())
      .updatedAt(Instant.now())
      .build();

    when(repo.findById(id)).thenReturn(Optional.of(existing));
    when(repo.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

    var uc = new UpdateProductUseCaseImpl(repo, indexer);
    uc.handle(id, new UpdateProductCmd("Novo", "D2", new BigDecimal("2.00"), "C2", BigInteger.TWO));

    verify(indexer, times(1)).index(any(Product.class));
  }
}
