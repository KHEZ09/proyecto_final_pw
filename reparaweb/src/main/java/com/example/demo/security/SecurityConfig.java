package com.example.demo.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final RoleSuccessHandler roleSuccessHandler;

    public SecurityConfig(RoleSuccessHandler roleSuccessHandler) {
        this.roleSuccessHandler = roleSuccessHandler;
    }

    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(12); }
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html", "/login.html", "/css/**", "/js/**", "/api/i18n/**", "/error", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/tecnico/**").hasAnyRole("TECNICO", "ADMINISTRADOR")
                .requestMatchers("/api/reportes/indicadores").hasAnyRole("TECNICO", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/api/reportes", "/api/reportes/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/reportes").hasAnyRole("USUARIO", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PATCH, "/api/reportes/**").hasAnyRole("TECNICO", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/reportes/**").hasAnyRole("TECNICO", "ADMINISTRADOR")
                .anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/login.html")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(roleSuccessHandler)
                .failureUrl("/login.html?error")
                .permitAll())
            .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, ignoredException) -> response.sendRedirect("/access-denied.html")))
            .logout(logout -> logout.logoutSuccessUrl("/login.html?logout"));
        return http.build();
    }
}
