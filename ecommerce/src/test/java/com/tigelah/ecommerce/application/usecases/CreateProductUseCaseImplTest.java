package com.tigelah.ecommerce.application.usecases;


import com.tigelah.ecommerce.application.product.command.CreateProductCmd;
import com.tigelah.ecommerce.application.product.ports.ProductIndexer;
import com.tigelah.ecommerce.application.product.usecase.impl.CreateProductUseCaseImpl;
import com.tigelah.ecommerce.domains.product.Product;
import com.tigelah.ecommerce.domains.product.ProductRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateProductUseCaseImplTest {

  @Test
  void deve_criar_produto_e_indexar_no_es() {
    var repo = mock(ProductRepository.class);
    var indexer = mock(ProductIndexer.class);

    when(repo.save(any(Product.class))).thenAnswer(inv -> {
      Product p = inv.getArgument(0);
      // simula retorno "salvo"
      return p.toBuilder().id(UUID.randomUUID()).build();
    });

    var uc = new CreateProductUseCaseImpl(repo, indexer);
    var id = uc.handle(new CreateProductCmd("Nome", "Desc", new BigDecimal("10.50"), "Cat", BigInteger.TEN));

    assertNotNull(id);
    verify(indexer, times(1)).index(any(Product.class));
  }
}
