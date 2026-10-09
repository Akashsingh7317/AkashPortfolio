package com.akash.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Must come first: "/contact/admin/**" is admin-only
                        .requestMatchers("/contact/admin/**")
                        .authenticated()

                        // Public Pages
                        .requestMatchers(
                                "/",
                                "/about",
                                "/projects",
                                "/project/**",
                                "/skills",
                                "/experience",
                                "/certificates",
                                "/contact",

                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/videos/**",
                                "/uploads/**",
                                "/icons/**",
                                "/fonts/**"

                        ).permitAll()

                        // Admin Pages (login page itself stays public)
                        .requestMatchers("/admin/login")
                        .permitAll()

                        .requestMatchers("/admin/**", "/contact/admin/**")
                        .authenticated()

                        .anyRequest()
                        .permitAll()

                )

                .formLogin(login -> login

                        .loginPage("/admin/login")

                        .loginProcessingUrl("/admin/login")

                        .defaultSuccessUrl("/admin/dashboard", true)

                        .failureUrl("/admin/login?error")

                        .permitAll()

                )

                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/")

                        .invalidateHttpSession(true)

                        .deleteCookies("JSESSIONID")

                        .permitAll()

                )

                .rememberMe(Customizer.withDefaults());

        return http.build();

    }

}