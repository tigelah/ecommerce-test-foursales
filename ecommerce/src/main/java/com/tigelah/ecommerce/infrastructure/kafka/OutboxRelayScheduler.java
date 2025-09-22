package com.tigelah.ecommerce.infrastructure.kafka;

import com.tigelah.ecommerce.application.order.ports.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class OutboxRelayScheduler {

  private final OutboxRepository outbox;
  private final KafkaProducerAdapter producer;

  @Scheduled(fixedDelay = 3000)
  public void relay() {
    for (var rec : outbox.findNextPending(50)) {
      try {
        var key = rec.aggregateId.toString();
        var topic = KafkaTopics.ORDER_PAID.equals(rec.type) ? KafkaTopics.ORDER_PAID : rec.type;
        producer.publish(topic, key, rec.payload);
        outbox.markPublished(rec.id);
      } catch (Exception e) {
        log.error("Falha ao publicar outbox {}", rec.id, e);
        outbox.markFailed(rec.id);
      }
    }
  }
}
