package com.mintpot.broadcasting.rest;

import com.mintpot.broadcasting.common.model.CustomUserDetails;
import com.mintpot.broadcasting.common.request.LoginReq;
import com.mintpot.broadcasting.common.response.JwtResponse;
import com.mintpot.broadcasting.configuration.security.JwtProvider;
import io.jsonwebtoken.impl.DefaultClaims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Api(tags = {"[Admin APIs] Authentication management"})
public class AuthenticationController extends AbstractController {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    @PostMapping("/authentication/signing")
    @ApiOperation(value = "Signing")
    public ResponseEntity<JwtResponse> authenticate(@Valid @RequestBody LoginReq loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getAuthorities()));
    }

    @GetMapping(value = "/authentication/refresh-token")
    @ApiOperation("Re-build refresh token")
    public ResponseEntity<Map<String, String>> refreshToken(HttpServletRequest request) {
        DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");
        Map<String, Object> expectedMap = jwtProvider.getMapFromIoJsonwebtokenClaims(claims);
        String token = jwtProvider.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        return ResponseEntity.ok(Map.of("accessToken", token));
    }
}
