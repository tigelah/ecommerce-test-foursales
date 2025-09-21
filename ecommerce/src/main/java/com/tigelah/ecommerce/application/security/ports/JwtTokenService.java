package com.tigelah.ecommerce.application.security.ports;

import java.util.Map;

public interface JwtTokenService {
  String generate(String subject, Map<String, Object> claims, long ttlSeconds);
  JwtTokenService.Parsed parse(String token);

  record Parsed(String subject, Map<String, Object> claims) {}
}
