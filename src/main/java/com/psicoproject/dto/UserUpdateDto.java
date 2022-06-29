package com.psicoproject.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.psicoproject.domain.Client;
import com.psicoproject.domain.User;
import com.psicoproject.domain.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserUpdateDto {
	
	@NotBlank(message = "Name required!")	
	private String username;
	
	@Size(min = 7, max = 99, message = "Password between 7 and 99 characters")
	private String password;
	
	@NotBlank(message = "Email required!")
	@Email(message = "Email invalid!")
	private String email;
	
	private List<Client> clients = new ArrayList<Client>();
	
	public User transformDto() {
		User user = new User(null, this.username, this.password, this.email, null, this.clients);
		return user;
		
	}
}
