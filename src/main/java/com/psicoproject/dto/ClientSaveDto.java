package com.psicoproject.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;

import com.psicoproject.domain.Client;
import com.psicoproject.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientSaveDto {
	
	@NotBlank(message = "Name required")
	@Size(max = 45)
	private String name;
	
	@NotNull(message = "Age required")
	private Integer age;
	
	@NotBlank(message = "occupation required")
	@Size(max = 45)
	private String occupation;

	@NotBlank(message = "email required")
	@Email(message = "invalid email")
	@Size(max = 100)
    private String email;
	
	@NotBlank(message = "phone required")
	@Size(max = 45)
    private String phone;
    
	@Size(max = 45)
	@NotBlank(message = "birthDate required")
	private String birthDate;
    
	@Size(max = 45)
	@NotBlank(message = "cpf required")
    private String cpf;
    
    private User user;

	public Client transformDto() {
		Client client = new Client(null, null, this.name, this.age, this.occupation, this.email, this.phone, this.birthDate, this.cpf, this.user);
		return client;
	}
}
