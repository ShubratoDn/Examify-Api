package com.api.examify.configs.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.examify.configs.security.CustomUserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	
	
	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		String header = request.getHeader("Authorization");
		String username = null ;
		String token = null;
		
		//if header is not null and Starts with bearer then fetchout the username from token
		if(header != null && header.startsWith("Bearer")) {
			token = header.substring(7);
			// token theke user name fetch korte hobe
			
			try {
				 username = jwtTokenUtil.extractUsername(token);
					log.info("User Found : "+ username);
					
			}catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT token (Filter Class theke)");
			}catch (ExpiredJwtException e) {
				System.out.println("JWT token has Expired (Filter Class theke)");
				
				// session expire hole msg
				response.setStatus(HttpStatus.FORBIDDEN.value());
				response.setContentType("application/json");
				String errorResponse = "{\"errorType\":\"TokenExpired\",\"error\":\"Token is expired\"}";
				response.getWriter().write(errorResponse);
				response.getWriter().flush();
				return;
			}catch (MalformedJwtException e) {
				System.out.println("Invalid JWT (from Filter Class)");
			} catch (Exception e) {
				System.out.println("USER NAME EXTRACT HOYNA");
			}			
		}else {
			System.out.println("The token doesn't starts with bearer");
		}
		
		
		

		// VALIDATING THE TOKEN
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);
			
			if(jwtTokenUtil.validateToken(token, userDetails)) {				
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);				
			}else {
				System.out.println("Faild validating the token");
			}			
		}else {
			System.out.println("Username not found from the TOKEN");
		}
		
		
		
		filterChain.doFilter(request, response);
		
	}	
	
}
