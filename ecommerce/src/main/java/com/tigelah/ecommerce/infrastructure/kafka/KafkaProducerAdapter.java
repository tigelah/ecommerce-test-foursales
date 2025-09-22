package com.tigelah.ecommerce.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducerAdapter {
  private final KafkaTemplate<String, String> kafka;

  public void publish(String topic, String key, String payload){
    kafka.send(new ProducerRecord<>(topic, key, payload));
  }
}
