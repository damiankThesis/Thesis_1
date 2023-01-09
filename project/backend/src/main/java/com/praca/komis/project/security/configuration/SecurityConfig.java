package com.praca.komis.project.security.configuration;

import com.praca.komis.project.model.RoleEnum;
import com.praca.komis.project.security.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        httpSecurity
                .headers().frameOptions().disable().and()       //h2-console
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .cors().and()
                .csrf().disable()
                .authorizeRequests(authorize -> authorize
                .antMatchers(HttpMethod.POST,"/api/v1/cars").hasRole(RoleEnum.ROLE_ADMIN.getRole())
                .antMatchers(HttpMethod.PUT,"/api/v1/cars").hasRole(RoleEnum.ROLE_ADMIN.getRole())
                .antMatchers(HttpMethod.DELETE,"/api/v1/cars").hasRole(RoleEnum.ROLE_ADMIN.getRole())
                .antMatchers(HttpMethod.PATCH,"/api/v1/cars").hasRole(RoleEnum.ROLE_ADMIN.getRole())
                .antMatchers(HttpMethod.DELETE,"/api/v1/users").hasRole(RoleEnum.ROLE_ADMIN.getRole())
                .antMatchers(HttpMethod.DELETE,"/api/v1/rent").hasRole(RoleEnum.ROLE_ADMIN.getRole())
                .antMatchers(HttpMethod.DELETE,"/api/v1/buy").hasRole(RoleEnum.ROLE_ADMIN.getRole())
                .anyRequest().permitAll()
        );

        httpSecurity.addFilter(new JwtAuthorizationFilter(authenticationManager, userDetailsService, secret));
        return httpSecurity.build();
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
