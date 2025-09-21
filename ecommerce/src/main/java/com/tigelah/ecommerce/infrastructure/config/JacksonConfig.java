package com.tigelah.ecommerce.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
  @Bean
  ObjectMapper objectMapper(){
    ObjectMapper om = new ObjectMapper();
    om.registerModule(new JavaTimeModule());
    return om;
  }
}
