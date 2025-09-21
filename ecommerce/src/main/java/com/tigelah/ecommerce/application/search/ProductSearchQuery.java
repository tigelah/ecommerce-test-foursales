package com.tigelah.ecommerce.application.search;

import java.util.List;

public interface ProductSearchQuery {
  record Criteria(String q, String category, Float minPrice, Float maxPrice, int page, int size) {}
  record Result(List<Item> items, long total){
    public record Item(String id, String name, String description, String category, float price, int stock){}
  }
  Result search(Criteria criteria);
}
