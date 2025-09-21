package com.tigelah.ecommerce.infrastructure.persistence.adpters;

import com.tigelah.ecommerce.domains.product.Product;
import com.tigelah.ecommerce.domains.product.ProductRepository;
import com.tigelah.ecommerce.infrastructure.persistence.jpa.ProductEntity;
import com.tigelah.ecommerce.infrastructure.persistence.jpa.SpringDataProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryJpaAdapter implements ProductRepository {

  private final SpringDataProductRepository repo;

  @Override
  public Optional<Product> findById(UUID id) {
    return repo.findById(id).map(this::toDomain);
  }

  @Override
  public Product save(Product product) {
    var entity = toEntity(product);
    if (entity.getId() == null) entity.setId(UUID.randomUUID());
    repo.save(entity);
    return toDomain(entity);
  }

  @Override
  public void deleteById(UUID id) {
    repo.deleteById(id);
  }

  private Product toDomain(ProductEntity e){
    return Product.builder()
      .id(e.getId())
      .name(e.getName())
      .description(e.getDescription())
      .price(e.getPrice())
      .category(e.getCategory())
      .stock(e.getStock())
      .createdAt(e.getCreatedAt())
      .updatedAt(e.getUpdatedAt())
      .build();
  }

  private ProductEntity toEntity(Product p){
    return ProductEntity.builder()
      .id(p.getId())
      .name(p.getName())
      .description(p.getDescription())
      .price(p.getPrice())
      .category(p.getCategory())
      .stock(p.getStock())
      .createdAt(p.getCreatedAt())
      .updatedAt(p.getUpdatedAt())
      .build();
  }
}
