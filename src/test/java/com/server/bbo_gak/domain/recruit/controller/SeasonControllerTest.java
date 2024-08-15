package com.server.bbo_gak.domain.recruit.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.server.bbo_gak.domain.recruit.dto.response.SeasonGetResponse;
import com.server.bbo_gak.domain.recruit.entity.Season;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.domain.user.entity.UserRepository;
import com.server.bbo_gak.domain.user.entity.UserRole;
import com.server.bbo_gak.global.AbstractRestDocsTests;
import com.server.bbo_gak.global.RestDocsFactory;
import com.server.bbo_gak.global.security.PrincipalDetails;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Sql("/season-test-data.sql")
public class SeasonControllerTest extends AbstractRestDocsTests {

    private static final String DEFAULT_URL = "/api/v1/seasons";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestDocsFactory restDocsFactory;
    private List<SeasonGetResponse> responses;

    @BeforeEach
    void setUp() {
        User user = userRepository.findById(1L).get();
        Season season1 = new Season("2024 하반기", user);
        Season season2 = new Season("2025 상반기", user);

        responses = List.of(
            SeasonGetResponse.from(season1),
            SeasonGetResponse.from(season2)
        );
    }

    @Nested
    class 분기리스트_조회 {

        @Test
        public void 분기_없으면_기본생성_성공() throws Exception {
            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL, null, HttpMethod.GET, objectMapper))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResourceList("[GET] 분기 리스트 조회 성공", "분기 리스트 조회", "Season",
                        List.of(), responses));
        }

        @Test
        public void 분기_있으면_DB에서_조회_성공() throws Exception {
            UserDetails userDetails = PrincipalDetails.ofJwt(2L, UserRole.USER);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User mockUser = User.builder()
                .id(2L)
                .role(UserRole.USER)
                .build();

            when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser));

            mockMvc.perform(
                    restDocsFactory.createRequest(DEFAULT_URL, null, HttpMethod.GET, objectMapper))
                .andExpect(status().isOk())
                .andDo(
                    restDocsFactory.getSuccessResourceList("[GET] 분기 리스트 조회 성공", "분기 리스트 조회", "Season",
                        List.of(), responses));
        }
    }

}
