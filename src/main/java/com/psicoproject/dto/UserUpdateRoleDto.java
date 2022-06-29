package com.psicoproject.dto;

import javax.validation.constraints.NotNull;

import com.psicoproject.domain.enums.Role;

import lombok.Data;
@Data
public class UserUpdateRoleDto {
	
	@NotNull(message = "Role required")
	private Role role;
}
