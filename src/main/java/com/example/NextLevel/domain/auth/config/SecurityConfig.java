package com.example.NextLevel.domain.auth.config;

import com.example.NextLevel.domain.auth.filter.JwtCheckFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtCheckFilter jwtCheckFilter;

    /**
     * 1) Actuator 전용 체인 (우선순위 1)
     */
    @Bean
    @Order(1)
    public SecurityFilterChain actuatorSecurityChain(HttpSecurity http) throws Exception {
        http
          .securityMatcher("/actuator/**")
          .csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(auth -> auth
              .anyRequest().permitAll()
          );
        return http.build();
    }

    /**
     * 2) API 전용 체인 (우선순위 2)
     */
    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityChain(HttpSecurity http) throws Exception {
        http
          .securityMatcher("/api/**")
          .csrf(AbstractHttpConfigurer::disable)
          .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authorizeHttpRequests(auth -> auth
              // 인증 없이 열어둘 API
              .requestMatchers(
                  "/api/auth/login",
                  "/api/members/signup",
                  "/api/problem-posts/all/**",
                  "/api/problem-posts/search/**",
                  "/local_image_storage/**",
                  "/problem_post_data_storage/**"
              ).permitAll()
              // GET만 공개할 경로
              .requestMatchers(HttpMethod.GET, "/api/team-recruits/**").permitAll()
              // 그 외 /api/** 는 JWT 인증
              .anyRequest().authenticated()
          )
          // JWT 검사 필터
          .addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class)
          // 전역 CORS
          .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    /**
     * 전역 CORS 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of("*"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH"));
        cfg.addAllowedHeader("*");
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }

    /**
     * BCrypt 비밀번호 인코더
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
