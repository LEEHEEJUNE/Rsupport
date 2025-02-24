package com.rspt.announcement.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class securityConfig(val tokenProvider: jwtTokenProvider) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // CSRF 비활성화: lambda DSL 사용
            .csrf { csrf -> csrf.disable() }
            // Stateless 세션 정책 적용
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            // 요청 권한 설정 ("/auth/**"는 인증 없이 접근 가능)
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers("/auth/login").permitAll()
                    .anyRequest().authenticated()
            }
            // JWT 인증 필터 추가 (UsernamePasswordAuthenticationFilter 전에 실행)
            .addFilterBefore(
                JwtAuthenticationFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }
}