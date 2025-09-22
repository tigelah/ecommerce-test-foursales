package com.tigelah.ecommerce.application.product.ports;


import com.tigelah.ecommerce.application.product.dto.ProductDTO;
import com.tigelah.ecommerce.application.product.query.ProductSearchQuery;
import com.tigelah.ecommerce.commons.PageResult;

public interface ProductSearchGateway {

  PageResult<ProductDTO> search(ProductSearchQuery query);
}
