package com.shop.login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfig {
	@Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Cấu hình UserDetailsService để quản lý user và admin trong bộ nhớ
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // Tài khoản user
        manager.createUser(
                User.withUsername("khach")
                    .password(passwordEncoder().encode("khach"))
                    .roles("USER") // Quyền USER
                    .build()
        );

        // Tài khoản admin
        manager.createUser(
                User.withUsername("admin")
                    .password(passwordEncoder().encode("admin"))
                    .roles("ADMIN") // Quyền ADMIN
                    .build()
        );

        return manager;
    }

    // Cấu hình PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cấu hình HttpSecurity
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/home").permitAll() // Cho phép truy cập không cần xác thực
                .requestMatchers("/admin/**").hasRole("ADMIN") // Chỉ ADMIN được phép truy cập /admin/**
                .requestMatchers("/user/**").hasRole("USER") // Chỉ USER được phép truy cập /user/**
                .anyRequest().authenticated() // Các URL khác yêu cầu xác thực
            )
            .formLogin(form -> form
                .successHandler(authenticationSuccessHandler()) // Tùy chỉnh chuyển hướng sau khi login
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll() // Cho phép logout
            )
            .csrf().disable(); // Tắt CSRF nếu không cần

        return http.build();
    }

    // Cấu hình AuthenticationSuccessHandler để chuyển hướng theo vai trò
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin");
            } else if (role.equals("ROLE_USER")) {
                response.sendRedirect("/hello");
            }
        };
    }
}
