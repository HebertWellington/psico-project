package com.psicoproject.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLoginResponseDto implements Serializable {
	private static final long serialVersionUID = -1173024866137135178L;
	
	private String token;
	private Long expireIn;
	private String tokenProvider;
}
