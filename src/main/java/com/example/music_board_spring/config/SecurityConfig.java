package com.example.music_board_spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class
SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize


                        // 누구나 접근 가능한 경로
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()

                        // Admin 전용 권한
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/boards/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/{userId}/activate", "/api/users/{userId}/deactivate").hasRole("ADMIN")

                        // 인증된 사용자 권한
                        .requestMatchers("/board/{boardName}/posts").authenticated()
                        .requestMatchers("/posts/{postId}/comments").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/board/{boardName}/posts/{postId}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/board/{boardName}/posts/{postId}").authenticated()
                        .requestMatchers("/api/users/{userId}").authenticated()

                        // 나머지 요청은 모두 허용
                        .anyRequest().permitAll()

                )
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (실제 환경에서는 활성화 권장)
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")// 로그인 페이지 주소
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")//로그인 성공 시 이동하는 곳
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults()) // HTTP Basic 인증 활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // 모든 도메인 허용
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}