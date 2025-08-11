// src/main/java/com/example/crudclinica/config/SecurityConfiguration.java
package com.example.crudclinica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/web/pacientes/novo", "/web/pacientes/salvar").permitAll()
                        .requestMatchers("/web/medicos/**", "/web/pacientes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/web/agenda").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/web/agenda/**").hasRole("ADMIN")
                        .requestMatchers("/web/consultas/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/access-denied")
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }


}