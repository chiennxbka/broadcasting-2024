package com.mintpot.broadcasting.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class JwtResponse {
  private String accessToken;
  private String type = "Bearer";
  private Collection<? extends GrantedAuthority> authorities;

  public JwtResponse(String accessToken, Collection<? extends GrantedAuthority> authorities) {
    this.accessToken = accessToken;
    this.authorities = authorities;
  }
}
