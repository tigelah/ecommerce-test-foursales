package com.tigelah.ecommerce.application.security;


import com.tigelah.ecommerce.application.security.ports.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtTokenService jwt;

  @Override
  protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
    throws IOException, ServletException {

    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      try {
        var parsed = jwt.parse(token);
        String userId = parsed.subject();
        String role = String.valueOf(parsed.claims().get("role"));
        var auth = SecurityConfig.authToken(userId, role);
        SecurityContextHolder.getContext().setAuthentication(auth);
      } catch (Exception ignored) {

      }
    }
    chain.doFilter(request, response);
  }
}
