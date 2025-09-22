package com.tigelah.ecommerce.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tigelah.ecommerce.application.inventory.usecase.ProcessOrderPaidUseCase;
import com.tigelah.ecommerce.domains.order.events.OrderPaidEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaidListener {

  private final ProcessOrderPaidUseCase usecase;
  private final ObjectMapper mapper;

  @KafkaListener(
    topics = KafkaTopics.ORDER_PAID,
    groupId = "${spring.kafka.consumer.group-id:ecommerce-inventory}",
    containerFactory = "kafkaListenerContainerFactory"
  )
  public void onMessage(@Payload String payload) {
    try {
      var evt = mapper.readValue(payload, OrderPaidEvent.class);
      usecase.handle(evt);
      log.info("Estoque atualizado para order {}", evt.orderId());
    } catch (Exception e) {
      log.error("Falha ao processar order.paid", e);

    }
  }
}
