package com.tigelah.ecommerce.infrastructure.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.tigelah.ecommerce.application.product.dto.ProductDTO;
import com.tigelah.ecommerce.application.product.ports.ProductSearchGateway;
import com.tigelah.ecommerce.application.product.query.ProductSearchQuery;
import com.tigelah.ecommerce.commons.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ProductSearchEsAdapter implements ProductSearchGateway {

  private static final String INDEX = "products";
  private final ElasticsearchClient es;

  @Override
  public PageResult<ProductDTO> search(ProductSearchQuery q) {
    try {
      final List<Query> must = new ArrayList<>();
      final List<Query> filter = new ArrayList<>();

      if (q.name() != null && !q.name().isBlank()) {
        must.add(Query.of(b -> b.multiMatch(m -> m.query(q.name()).fields("name^3", "description").fuzziness("AUTO").operator(Operator.And))));
      }

      if (q.category() != null && !q.category().isBlank()) {
        filter.add(Query.of(b -> b.term(t -> t.field("category").value(v -> v.stringValue(q.category())))));
      }

      filter.add(Query.of(b -> b.range(r -> r.field("stock").gt(JsonData.of(0)))));

      if (q.minPrice() != null || q.maxPrice() != null) {
        filter.add(Query.of(b -> b.range(r -> {
          r.field("price");
          if (q.minPrice() != null) r.gte(JsonData.of(q.minPrice()));
          if (q.maxPrice() != null) r.lte(JsonData.of(q.maxPrice()));
          return r;
        })));
      }

      Query boolQuery = Query.of(b -> b.bool(bb -> {
        if (!must.isEmpty()) bb.must(must);
        if (!filter.isEmpty()) bb.filter(filter);
        return bb;
      }));

      SearchRequest request = SearchRequest.of(sr -> sr.index(INDEX).from(q.page() * q.size()).size(q.size()).sort(s -> s.field(f -> f.field("updatedAt").order(SortOrder.Desc))).query(boolQuery));

      @SuppressWarnings("unchecked") SearchResponse<Map<String, Object>> resp = es.search(request, (Class<Map<String, Object>>) (Class<?>) Map.class);

      long total = resp.hits().total() != null ? resp.hits().total().value() : 0;

      List<ProductDTO> items = resp.hits().hits().stream().map(hit -> {
        Map<String, Object> doc = hit.source();
        if (doc == null) return null;
        UUID id = null;
        Object idRaw = doc.get("id");
        if (idRaw != null) {
          try {
            id = (idRaw instanceof UUID u) ? u : UUID.fromString(String.valueOf(idRaw));
          } catch (Exception ignored) {
          }
        }

        BigDecimal price = null;
        Object priceRaw = doc.get("price");
        if (priceRaw != null) {
          if (priceRaw instanceof BigDecimal bd) price = bd;
          else price = new BigDecimal(String.valueOf(priceRaw));
        }

        BigInteger stock = null;
        Object stockRaw = doc.get("stock");
        if (stockRaw != null) {
          if (stockRaw instanceof BigInteger bi) stock = bi;
          else stock = new BigInteger(String.valueOf(stockRaw));
        }

        return new ProductDTO(id, (String) doc.get("name"), (String) doc.get("description"), price, (String) doc.get("category"), stock);
      }).filter(Objects::nonNull).toList();

      return new PageResult<>(items, total, q.page(), q.size());

    } catch (Exception e) {
      throw new RuntimeException("Falha na busca no Elasticsearch", e);
    }
  }
}
