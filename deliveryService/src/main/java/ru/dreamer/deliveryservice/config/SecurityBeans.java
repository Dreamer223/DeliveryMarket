package ru.dreamer.deliveryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityBeans {

    @Bean
    public SecurityFilterChain configureSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"catalogue/products")
                .hasAuthority("SCOPE_edit_catalogue")
                .requestMatchers(HttpMethod.PATCH,"catalogue/products/{productId:\\d+}")
                                .hasAuthority("SCOPE_edit_catalogue")
                                .requestMatchers(HttpMethod.DELETE,"catalogue/products/{productId:\\d+}")
                                .hasAuthority("SCOPE_edit_catalogue")
                                .requestMatchers(HttpMethod.GET)
                                .hasAuthority("SCOPE_view_catalogue")
                                .anyRequest().permitAll()
                )
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
}
