package com.server.bbo_gak.global.security.jwt.filter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.server.bbo_gak.domain.user.controller.UserController;
import com.server.bbo_gak.domain.user.entity.UserRole;
import com.server.bbo_gak.global.error.exception.ErrorCode;
import com.server.bbo_gak.global.security.jwt.dto.AccessTokenDto;
import com.server.bbo_gak.global.security.jwt.service.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private UserController memberController;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController) // 컨트롤러 추가
            .addFilters(jwtAuthenticationFilter)
            .build();
    }

    @Test
    public void 유효한_액세스_토큰_테스트() throws Exception {
        String accessToken = "validAccessToken";
        AccessTokenDto accessTokenDto = new AccessTokenDto(1L, UserRole.USER, accessToken);

        when(jwtTokenService.validateAccessToken(accessToken)).thenReturn(true);
        when(jwtTokenService.retrieveAccessToken(accessToken)).thenReturn(accessTokenDto);

        mockMvc.perform(get("/api/v1/users/me")
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk());

        verify(jwtTokenService, times(1)).validateAccessToken(accessToken);
        verify(jwtTokenService, times(1)).retrieveAccessToken(accessToken);
    }

    @Test
    public void 만료된_액세스_토큰_리프레시_토큰_없음() {
        String expiredAccessToken = "expiredAccessToken";

        when(jwtTokenService.validateAccessToken(expiredAccessToken)).thenThrow(
            new ExpiredJwtException(null, null, "Expired"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        request.addHeader("Authorization", "Bearer " + expiredAccessToken);
        assertThrows(JwtException.class,
            () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));

        verify(jwtTokenService, times(1)).validateAccessToken(expiredAccessToken);
    }


    @Test
    public void 유효하지_않은_액세스_토큰() throws Exception {
        String invalidAccessToken = "invalidAccessToken";

        when(jwtTokenService.validateAccessToken(invalidAccessToken))
            .thenThrow(new JwtException(ErrorCode.WRONG_TYPE_TOKEN.getMessage()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        request.addHeader("Authorization", "Bearer " + invalidAccessToken);
        assertThrows(JwtException.class,
            () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));

        verify(jwtTokenService, times(1)).validateAccessToken(invalidAccessToken);
    }
}
