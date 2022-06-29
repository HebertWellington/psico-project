package com.psicoproject.security;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Component;

import com.psicoproject.constant.SecurityConstant;
import com.psicoproject.dto.UserLoginResponseDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtManager {
	
	public UserLoginResponseDto createToken(String email, List<String> roles) {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, SecurityConstant.JWT_EXP_DAYS);
		
		String jwt = Jwts.builder()
						 .setSubject(email)
						 .setExpiration(cal.getTime())
						 .claim(SecurityConstant.JWT_ROLE_KEY, roles)
						 .signWith(SignatureAlgorithm.HS512, SecurityConstant.API_KEYS.getBytes())
						 .compact();
		
		Long expireIn = cal.getTimeInMillis();
		
		return new UserLoginResponseDto(jwt, expireIn, SecurityConstant.JTW_PROVIDER) ;
	}
	
	public Claims parseToken(String jwt) throws JwtException {
		Claims claims = Jwts.parser()
							.setSigningKey(SecurityConstant.API_KEYS.getBytes())
							.parseClaimsJws(jwt)
							.getBody();
		return claims;
	}
}
