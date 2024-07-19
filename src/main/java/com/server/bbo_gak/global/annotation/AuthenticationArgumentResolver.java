package com.server.bbo_gak.global.annotation;

import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.domain.user.entity.UserRepository;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.error.exception.NotFoundException;
import com.server.bbo_gak.global.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        final boolean isUserAuthAnnotation = parameter.getParameterAnnotation(AuthUser.class) != null;
        final boolean isMemberClass = parameter.getParameterType().equals(User.class);
        return isUserAuthAnnotation && isMemberClass;
    }

    @Override
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return getCurrentMember();
    }

    private User getCurrentMember() {
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
