package com.tigelah.ecommerce.entrypoints.http;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
  @GetMapping({"/health","/actuator/ping"})
  public String health(){ return "OK"; }
}
