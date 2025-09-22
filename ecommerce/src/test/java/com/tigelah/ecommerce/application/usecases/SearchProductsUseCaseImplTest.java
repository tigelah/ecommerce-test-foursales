package com.tigelah.ecommerce.application.usecases;

import com.tigelah.ecommerce.commons.PageResult;
import com.tigelah.ecommerce.application.product.dto.ProductDTO;
import com.tigelah.ecommerce.application.product.ports.ProductSearchGateway;
import com.tigelah.ecommerce.application.product.query.ProductSearchQuery;
import com.tigelah.ecommerce.application.product.usecase.impl.SearchProductsUseCaseImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchProductsUseCaseImplTest {

  @Test
  void deve_validar_faixa_de_preco() {
    var gw = mock(ProductSearchGateway.class);
    var uc = new SearchProductsUseCaseImpl(gw);

    var q = new ProductSearchQuery(null, null, new BigDecimal("100"), new BigDecimal("50"), 0, 10);
    assertThrows(IllegalArgumentException.class, () -> uc.handle(q));
    verifyNoInteractions(gw);
  }

  @Test
  void deve_delegar_ao_gateway_e_retornar_paginado() {
    var gw = mock(ProductSearchGateway.class);
    var uc = new SearchProductsUseCaseImpl(gw);

    var q = new ProductSearchQuery("camisa", "VESTUARIO", new BigDecimal("10"), new BigDecimal("200"), 0, 2);

    var dto = new ProductDTO(UUID.randomUUID(), "Camisa", "algodão", new BigDecimal("59.90"), "VESTUARIO", BigInteger.TEN);
    when(gw.search(q)).thenReturn(new PageResult<>(List.of(dto), 1, 0, 2));

    var res = uc.handle(q);
    assertEquals(1, res.total());
    assertEquals(1, res.content().size());
    assertEquals("Camisa", res.content().get(0).name());
    verify(gw, times(1)).search(q);
  }
}
