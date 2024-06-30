package com.server.bbo_gak.global.auth.util;

import com.vivid.apiserver.global.error.exception.AccessDeniedException;
import com.vivid.apiserver.global.error.exception.ErrorCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor
public class TokenUtil {

    public static String getCurrentUserEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (validateAuthentication(authentication)) {
            throw new AccessDeniedException(ErrorCode.LOGIN_INFO_NOT_FOUND);
        }

        return authentication.getName();
    }

    private static boolean validateAuthentication(Authentication authentication) {
        return authentication == null || authentication.getName() == null;
    }
}
