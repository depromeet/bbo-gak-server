package com.server.bbo_gak.global.config;

import com.vivid.apiserver.domain.user.domain.Role;
import com.vivid.apiserver.global.auth.JwtAuthExceptionFilter;
import com.vivid.apiserver.global.auth.JwtAuthFilter;
import com.vivid.apiserver.global.auth.RestAuthenticationEntryPoint;
import com.vivid.apiserver.global.auth.application.OAuthSuccessHandler;
import com.vivid.apiserver.global.auth.application.TokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
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
        http.cors().configurationSource(configurationSource());

        http.httpBasic().disable()
                .csrf().disable()
                .formLogin().disable() // 로그인 폼 미사용
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint()) // 인증,인가가 되지 않은 요청 시 발생시
                .and()
                .authorizeRequests()
                .antMatchers("/"
                        , "/css/**"
                        , "/images/**"
                        , "/js/**"
                        , "/h2-console/**"
                ).permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // 추가
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                .antMatchers("/api/login/**", "/api/auth/**", "/success-login").permitAll() // Security 허용 url
                .antMatchers("/api/**").hasRole(Role.USER.name())   // 모든 api 요청에 대해 user 권한
                .anyRequest().authenticated()   // 나머지 요청에 대해서 권한이 있어야함
                .and()
                .oauth2Login()
                .successHandler(oAuthSuccessHandler)
                .userInfoEndpoint().userService(auth2UserService)
                .and()
                .authorizationEndpoint().baseUri("/login"); // 소셜 로그인 url

        http
                .addFilterBefore(new JwtAuthFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthExceptionFilter(), JwtAuthFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
