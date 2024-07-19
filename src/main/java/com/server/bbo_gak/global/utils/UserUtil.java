package com.server.bbo_gak.global.utils;

import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.domain.user.entity.UserRepository;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import com.server.bbo_gak.global.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        return userRepository
            .findById(getCurrentMemberId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private Long getCurrentMemberId() {
        try {
            PrincipalDetails customUserDetails =
                (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Long.valueOf(customUserDetails.getUsername());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.AUTH_NOT_FOUND);
        }
    }
}
