package com.mintpot.broadcasting.configuration.security;

import com.mintpot.broadcasting.common.model.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
  private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

  @Value("${spring.app.jwt.secret}")
  private String jwtSecret;

  @Value("${spring.app.jwt.expiration}")
  private long jwtAccessTokenValidity;

  @Value("${spring.app.jwt.refresh.expiration}")
  private long refreshTokenDuration;

  boolean validateJwtToken(String authToken, HttpServletRequest request) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token -> Message: {0}", e);
    } catch (ExpiredJwtException e) {
      logger.error("Expired JWT token -> Message: {0}", e);
      String requestURL = request.getRequestURL().toString();
      // allow for Refresh Token creation if following conditions are true.
      if (requestURL.contains("auth/refresh-token")) {
        allowForRefreshToken(e, request);
      } else request.setAttribute("exception", e);
    } catch (UnsupportedJwtException e) {
      logger.error("Unsupported JWT token -> Message: {0}", e);
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty -> Message: {0}", e);
    }
    return false;
  }

  String getSubjectFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        null, null, null);
    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    request.setAttribute("claims", ex.getClaims());
  }

  public String generateJwtToken(Authentication authentication) {
    CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
    return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(Date.from(Instant.now()))
        .setExpiration(Date.from(Instant.now().plusSeconds(jwtAccessTokenValidity)))
        .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
  }

  public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date.from(Instant.now()))
        .setExpiration(Date.from(Instant.now().plusSeconds(refreshTokenDuration)))
        .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
  }

  public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
    Map<String, Object> expectedMap = new HashMap<>();
    for (Map.Entry<String, Object> entry : claims.entrySet()) {
      expectedMap.put(entry.getKey(), entry.getValue());
    }
    return expectedMap;
  }
}
