package com.tigelah.ecommerce.application.product.usecase;

import com.tigelah.ecommerce.application.product.dto.ProductDTO;
import com.tigelah.ecommerce.application.product.query.ProductSearchQuery;
import com.tigelah.ecommerce.commons.PageResult;

public interface SearchProductsUseCase {
  PageResult<ProductDTO> handle(ProductSearchQuery query);
}
