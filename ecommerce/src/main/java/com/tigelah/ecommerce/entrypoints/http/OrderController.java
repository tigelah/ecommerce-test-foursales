package com.tigelah.ecommerce.entrypoints.http;

import com.tigelah.ecommerce.application.order.command.CreateOrderCmd;
import com.tigelah.ecommerce.application.order.command.PayOrderCommand;
import com.tigelah.ecommerce.application.order.usecase.CreateOrderUseCase;
import com.tigelah.ecommerce.application.order.usecase.PayOrderUseCase;
import com.tigelah.ecommerce.domains.order.Order;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final CreateOrderUseCase createOrder;
  private final PayOrderUseCase payOrder;

  @PreAuthorize("hasRole('USER')")
  @PostMapping
  public ResponseEntity<Order> create(@RequestBody @Valid CreateOrderCmd body, Principal principal) {
    // subject do JWT = userId
    var userId = UUID.fromString(principal.getName());
    var cmd = new CreateOrderCmd(userId, body.items());
    var order = createOrder.handle(cmd);
    return ResponseEntity.ok(order);
  }

  @PreAuthorize("hasRole('USER')")
  @PostMapping("/{orderId}/pay")
  public ResponseEntity<Order> pay(@PathVariable UUID orderId, Principal principal){
    var userId = UUID.fromString(principal.getName());
    var order = payOrder.handle(new PayOrderCommand(orderId, userId));
    return ResponseEntity.ok(order);
  }
}
