package com.server.bbo_gak.global.config;


import java.util.List;

import com.server.bbo_gak.global.auth.JwtAuthExceptionFilter;
import com.server.bbo_gak.global.auth.JwtAuthFilter;
import com.server.bbo_gak.global.auth.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserService auth2UserService;

    private final OAuthSuccessHandler oAuthSuccessHandler;

    private final TokenProvider tokenProvider;

    String[] allowUrls = {"/", "/swagger-ui/**", "/v3/**", "/docs.html", "/api-docs/**", "/member/login", "/member/teacher/signUp", "/member/student/signUp"};

    @Bean
    public WebSecurityCustomizer configure() {

        // filter 안타게끔
        return (web) -> web.ignoring().mvcMatchers(
                "/api/auth/token/**", "/swagger-ui/**", "/api/test/**"
                , "/v3/api-docs/**", "/login/oauth2/code", "/api/login/webex",
                "/api/videos/{video_id}/uploaded", "/swagger-resources/**", "/success-login"
        );
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(List.of("https://edu-vivid.com", "https://dev.edu-vivid.com", "http://localhost:8081"));
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(List.of("*"));
        cors.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // cors 관련 설정

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(customizer -> customizer.configurationSource(configurationSource()))
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint()))// 인증,인가가 되지 않은 요청 시 발생시
                .authorizeHttpRequests(request -> request
                    .requestMatchers(allowUrls).permitAll()
                    .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthExceptionFilter(), JwtAuthFilter.class);

        http
                .oauth2Login()
                .successHandler(oAuthSuccessHandler)
                .userInfoEndpoint().userService(auth2UserService)
                .authorizationEndpoint().baseUri("/login"); // 소셜 로그인 url


        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
