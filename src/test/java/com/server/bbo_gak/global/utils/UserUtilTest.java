package com.server.bbo_gak.global.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.domain.user.entity.UserRepository;
import com.server.bbo_gak.domain.user.entity.UserRole;
import com.server.bbo_gak.global.error.exception.BusinessException;
import com.server.bbo_gak.global.security.PrincipalDetails;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class UserUtilTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUtil userUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Auth에서_현재_유저_가져오기() {
        // Given
        User user = new User("name", "testUser", UserRole.USER);
        setField(user, "id", 1L); // 리플렉션을 사용하여 ID 설정

        PrincipalDetails principalDetails = new PrincipalDetails(user.getId(), user.getRole());

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principalDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        User result = userUtil.getCurrentUser();

        // Then
        assertEquals(user, result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void Auth정보_없음() {
        // Given
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);

        SecurityContextHolder.setContext(securityContext);

        // When & Then
        assertThrows(BusinessException.class, () -> userUtil.getCurrentUser());
    }
}