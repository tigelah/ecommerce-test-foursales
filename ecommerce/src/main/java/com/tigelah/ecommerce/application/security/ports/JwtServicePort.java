package com.tigelah.ecommerce.application.security.ports;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtServicePort implements JwtTokenService {

  private final Key key;

  public JwtServicePort(@Value("${security.jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public String generate(String subject, Map<String, Object> claims, long ttlSeconds) {
    long now = System.currentTimeMillis();
    return Jwts.builder().setSubject(subject).addClaims(claims).setIssuedAt(new Date(now)).setExpiration(new Date(now + ttlSeconds * 1000)).signWith(key, SignatureAlgorithm.HS256).compact();
  }

  @Override
  public Parsed parse(String token) {
    Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    Claims c = jws.getBody();
    return new Parsed(c.getSubject(), c);
  }
}
