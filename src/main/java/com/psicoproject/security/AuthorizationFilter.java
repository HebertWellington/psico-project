package com.psicoproject.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psicoproject.constant.SecurityConstant;
import com.psicoproject.resource.exception.ApiError;

import io.jsonwebtoken.Claims;

public class AuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (jwt == null || !jwt.startsWith(SecurityConstant.JTW_PROVIDER)) {
			ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), SecurityConstant.JWT_INVALID_MSG,
					new Date());
			PrintWriter writer = response.getWriter();

			ObjectMapper mapper = new ObjectMapper();
			String apiErrorString = mapper.writeValueAsString(apiError);

			writer.write(apiErrorString);

			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());

			return;
		}

		jwt = jwt.replace(SecurityConstant.JTW_PROVIDER, "");

		try {
			Claims claims = new JwtManager().parseToken(jwt);
			String email = claims.getSubject();
			List<String> roles = (List<String>) claims.get(SecurityConstant.JWT_ROLE_KEY);

			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			roles.forEach(role -> {
				authorities.add(new SimpleGrantedAuthority(role));
			});

			Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (Exception e) {
			ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), SecurityConstant.JWT_INVALID_MSG,
					new Date());
			PrintWriter writer = response.getWriter();

			ObjectMapper mapper = new ObjectMapper();
			String apiErrorString = mapper.writeValueAsString(apiError);

			writer.write(apiErrorString);

			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());

			return;
		}

		filterChain.doFilter(request, response);
	}
}
