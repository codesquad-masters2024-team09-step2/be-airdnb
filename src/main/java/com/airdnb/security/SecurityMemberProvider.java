package com.airdnb.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityMemberProvider {
    public static String getCurrentMemberId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
