package com.crispy.challenge.todoapp.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class OwnerUtils {

    private OwnerUtils() {}

    public static Long getOwnerId() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }
}
