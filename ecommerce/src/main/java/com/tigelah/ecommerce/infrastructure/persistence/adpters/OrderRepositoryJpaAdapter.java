package com.tigelah.ecommerce.infrastructure.persistence.adpters;

import com.tigelah.ecommerce.application.order.ports.OrderRepository;
import com.tigelah.ecommerce.domains.order.Order;
import com.tigelah.ecommerce.domains.order.OrderItem;
import com.tigelah.ecommerce.domains.order.OrderStatus;
import com.tigelah.ecommerce.infrastructure.persistence.jpa.entities.OrderEntity;
import com.tigelah.ecommerce.infrastructure.persistence.jpa.entities.OrderItemEntity;
import com.tigelah.ecommerce.infrastructure.persistence.jpa.repositories.SpringDataOrderJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Primary
@RequiredArgsConstructor
public class OrderRepositoryJpaAdapter implements OrderRepository {

  private final SpringDataOrderJpa jpa;

  @Override
  public Order save(Order order) {
    var e = toEntity(order);
    if (e.getId() == null) e.setId(UUID.randomUUID());
    e.getItems().forEach(i -> i.setOrder(e));
    return toDomain(jpa.save(e));
  }

  @Override
  public java.util.Optional<Order> findById(UUID id) {
    return jpa.findById(id).map(this::toDomain);
  }

  @Override
  public void update(Order order) {
    var e = toEntity(order);
    e.getItems().forEach(i -> i.setOrder(e));
    jpa.save(e);
  }

  private OrderEntity toEntity(Order o){
    return OrderEntity.builder()
      .id(o.getId())
      .userId(o.getUserId())
      .status(o.getStatus().name())
      .total(o.getTotal())
      .createdAt(o.getCreatedAt())
      .paidAt(o.getPaidAt())
      .items(o.getItems().stream().map(it ->
        OrderItemEntity.builder()
          .productId(it.productId())
          .quantity(it.quantity())
          .price(it.unitPrice())
          .build()
      ).collect(Collectors.toList()))
      .build();
  }

  private Order toDomain(OrderEntity e){
    var items = e.getItems().stream().map(i ->
      new OrderItem(i.getProductId(), i.getQuantity(), i.getPrice())
    ).collect(Collectors.toList());
    return Order.builder()
      .id(e.getId())
      .userId(e.getUserId())
      .items(items)
      .status(OrderStatus.valueOf(e.getStatus()))
      .total(e.getTotal())
      .createdAt(e.getCreatedAt())
      .paidAt(e.getPaidAt())
      .build();
  }
}
