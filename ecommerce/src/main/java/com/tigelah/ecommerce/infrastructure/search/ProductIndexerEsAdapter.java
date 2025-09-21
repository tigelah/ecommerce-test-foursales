package com.tigelah.ecommerce.infrastructure.search;

import com.tigelah.ecommerce.application.product.ports.ProductIndexer;
import com.tigelah.ecommerce.domains.product.Product;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductIndexerEsAdapter implements ProductIndexer {

  private final ElasticsearchClient es;
  private static final String INDEX = "products";

  @Override
  public void index(Product p) {
    try {
      var doc = Map.of(
        "id", p.getId().toString(),
        "name", p.getName(),
        "description", p.getDescription(),
        "price", p.getPrice(),
        "category", p.getCategory(),
        "stock", p.getStock(),
        "createdAt", p.getCreatedAt().toString(),
        "updatedAt", p.getUpdatedAt().toString()
      );
      es.index(IndexRequest.of(r -> r.index(INDEX).id(p.getId().toString()).document(doc)));
    } catch (IOException e) {
      throw new RuntimeException("Falha ao indexar produto no ES", e);
    }
  }

  @Override
  public void delete(UUID id) {
    try {
      es.delete(DeleteRequest.of(r -> r.index(INDEX).id(id.toString())));
    } catch (IOException ignored) {

    }
  }
}
