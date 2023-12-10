package com.mintpot.broadcasting.configuration.security.services;

import com.mintpot.broadcasting.common.model.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {
  @Override
  public Optional<Long> getCurrentAuditor() {
    if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
      CustomUserDetails auditor = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      return Optional.ofNullable(auditor.getUser().getId());
    }
    return Optional.empty();
  }
}
