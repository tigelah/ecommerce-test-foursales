package com.tigelah.ecommerce.commons;

import java.util.List;

public record PageResult<T>(List<T> content, long total, int page, int size) {
  public static <T> PageResult<T> empty(int page, int size){ return new PageResult<>(List.of(), 0, page, size); }
}
