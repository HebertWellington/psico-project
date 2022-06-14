package com.psicoproject.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class UserLoginDto {
	
	@Size(min = 7, max = 99, message = "Password between 7 and 99 characters")
	private String password;
	
	@Email(message = "Email invalid!")
	@NotBlank(message = "Email required!")
	private String email;
}
