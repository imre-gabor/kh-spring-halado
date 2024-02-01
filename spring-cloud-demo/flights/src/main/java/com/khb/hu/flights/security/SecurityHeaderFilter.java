package com.khb.hu.flights.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Component
public class SecurityHeaderFilter extends OncePerRequestFilter{
	
	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer ";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String username = request.getHeader("x-jwt-username");
		String authCsv = request.getHeader("x-jwt-auth");

		if(StringUtils.hasLength(username) && StringUtils.hasLength(authCsv)){
			List<SimpleGrantedAuthority> authorities = Stream.of(authCsv.split(",")).map(SimpleGrantedAuthority::new).toList();
			User userDetails = new User(username, "dummy", authorities);

			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());

			usernamePasswordAuthenticationToken
					.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}

		filterChain.doFilter(request, response);
	}
}
