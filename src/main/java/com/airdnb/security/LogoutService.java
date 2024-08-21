package com.airdnb.security;

import com.airdnb.global.constants.SecurityConstants;
import com.airdnb.security.jwt.JwtBlacklistService;
import com.airdnb.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final JwtBlacklistService jwtBlacklistService;

    private final JwtUtil jwtUtil;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = jwtUtil.getToken(request.getHeader(SecurityConstants.AUTHORIZATION_HEADER));
        if (token != null && jwtUtil.validateToken(token)) {
            jwtBlacklistService.blacklistToken(token, jwtUtil.getRemainingTime(token));
        }
        SecurityContextHolder.clearContext();
    }
}
