package com.api.examify.configs.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.api.examify.configs.jwt.JWTAuthenticationEntryPoint;
import com.api.examify.configs.jwt.JWTAuthenticationFilter;
import com.api.examify.exceptions.CustomAccessDeniedHandler;

@Configuration
@EnableWebSecurity

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private JWTAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private JWTAuthenticationFilter authenticationFilter;
	
	// List of whitelisted endpoints that are allowed without authentication
	public static final String[] ENDPOINTS_WHITELIST = {
        "/api/v1/auth/login",
        "/api/v1/auth/register",
        "/api/v1/test",
        "UserImages/**",
        "/api/v1/public/**",
        "/api/v1/auth/delete/user/**",
    };	
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors() // Enable Cross-Origin Resource Sharing (CORS)
			.and().csrf().disable() // Disable CSRF protection
			.authorizeHttpRequests(auth ->
				auth.requestMatchers(ENDPOINTS_WHITELIST).permitAll() // Permit whitelisted endpoints without authentication
					.requestMatchers("/login").permitAll()
					.anyRequest().authenticated()) // Require authentication for other endpoints
			.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint) // Configure authentication entry point for handling unauthenticated requests
				.accessDeniedHandler(new CustomAccessDeniedHandler()) // Set custom handler for access denied responses
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless sessions
			;
		
		// Configure authentication provider
		http.authenticationProvider(authenticationProvider());
		
		// Configure JWT filter
		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		DefaultSecurityFilterChain build = http.build();
		return build;
	}
	
	@Autowired
	CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customUserDetailsServiceImpl); // Set custom user details service
		authenticationProvider.setPasswordEncoder(this.passwordEncoder()); // Set password encoder
		return authenticationProvider;
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Use BCryptPasswordEncoder for password hashing
	}
	
	@Bean
	AccessDeniedHandler myAccessDeniedHandler() {
		return new CustomAccessDeniedHandler(); // Create and configure custom access denied handler
	}

	@Bean
	AuthenticationManager authenticationManager() {
		ProviderManager providerManager = new ProviderManager(Collections.singletonList(authenticationProvider()));
		return providerManager; // Configure and return authentication manager
	}
}
