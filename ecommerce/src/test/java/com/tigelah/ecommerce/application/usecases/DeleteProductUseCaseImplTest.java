package com.tigelah.ecommerce.application.usecases;

import com.tigelah.ecommerce.application.product.ports.ProductIndexer;
import com.tigelah.ecommerce.application.product.usecase.impl.DeleteProductUseCaseImpl;
import com.tigelah.ecommerce.domains.product.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

class DeleteProductUseCaseImplTest {
  @Test
  void deve_deletar_e_remover_do_es(){
    var repo = mock(ProductRepository.class);
    var idx  = mock(ProductIndexer.class);
    var uc = new DeleteProductUseCaseImpl(repo, idx);

    var id = UUID.randomUUID();
    uc.handle(id);

    verify(repo, times(1)).deleteById(id);
    verify(idx, times(1)).delete(id);
  }
}
