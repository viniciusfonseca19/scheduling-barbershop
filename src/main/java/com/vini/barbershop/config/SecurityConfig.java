package com.vini.barbershop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // habilita @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // desabilita csrf (API)
                .csrf(csrf -> csrf.disable())

                // regras de acesso
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/usuarios/**").permitAll() // cadastro liberado
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                )

                // login padrão do Spring
                .formLogin(form -> form
                        .permitAll()
                )

                // logout
                .logout(logout -> logout
                        .permitAll()
                );

        return http.build();
    }
}