package com.tigelah.ecommerce.application.product.usecase;

import com.tigelah.ecommerce.commons.PageResult;
import com.tigelah.ecommerce.application.product.dto.ProductDTO;
import com.tigelah.ecommerce.application.product.query.ProductSearchQuery;

public interface SearchProductsUseCase {
  PageResult<ProductDTO> handle(ProductSearchQuery query);
}
