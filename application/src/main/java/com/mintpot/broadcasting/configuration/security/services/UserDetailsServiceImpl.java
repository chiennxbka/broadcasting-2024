package com.mintpot.broadcasting.configuration.security.services;

import com.mintpot.broadcasting.common.enums.ErrorCode;
import com.mintpot.broadcasting.common.exception.BusinessException;
import com.mintpot.broadcasting.common.model.CustomUserDetails;
import com.mintpot.broadcasting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException(ErrorCode.AUTHENTICATE_INVALID));
        var customUserDetails = new CustomUserDetails();
        if (user.getRole() != null) {
            var role = user.getRole();
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role.getType()));
            customUserDetails.setAuthorities(authorities);
        }
        customUserDetails.setUser(user);
        return customUserDetails;
    }
}
