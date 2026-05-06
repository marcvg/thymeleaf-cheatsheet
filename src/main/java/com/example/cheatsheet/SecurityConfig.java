package com.example.cheatsheet;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadWebApplicationHttpSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;


@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Chain 1 (REST API + actuator) lives in an external jar — no @Order there, defaults to
    // Integer.MAX_VALUE. Our UI chains only match /ui/**, so everything else falls through
    // to the external chain naturally. Actuator paths are permitted via YAML-configured anonymous paths there.

    /**
     * Chain 2 — Web UI: NOOP (default, active when profile "azure-ui-auth" is NOT set)
     *
     * No authentication, no CSRF. Safe for local development and early-phase work.
     * Switch to Chain 3 by activating the "azure-ui-auth" Spring profile.
     */
    @Bean
    @Order(2)
    @Profile("!azure-ui-auth")
    public SecurityFilterChain htmlNoopFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/ui/**")
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll());
        return http.build();
    }

    /**
     * Chain 3 — Web UI: Azure AD OIDC (active when profile "azure-ui-auth" is set)
     *
     * Uses OIDC login via App Reg 2 (credential.client-id / client-secret).
     * Only /secured/** requires authentication; all other routes are public.
     * Unauthenticated access to /secured/** redirects directly to Azure AD SSO
     * (skipping Spring's intermediate login page).
     */
    @Bean
    @Order(2)
    @Profile("azure-ui-auth")
    public SecurityFilterChain htmlAzureFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/ui/**")
            .with(AadWebApplicationHttpSecurityConfigurer.aadWebApplication(), Customizer.withDefaults())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/ui/secured/**").authenticated()
                .anyRequest().permitAll())
            // Skip Spring's generated login page — go straight to Azure AD
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/oauth2/authorization/azure")));
        return http.build();
    }
}
