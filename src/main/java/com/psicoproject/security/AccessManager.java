package com.psicoproject.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.psicoproject.domain.Client;
import com.psicoproject.domain.User;
import com.psicoproject.dto.ClientSaveDto;
import com.psicoproject.exception.NotFoundException;
import com.psicoproject.model.PageModel;
import com.psicoproject.repository.UserRepository;
import com.psicoproject.service.ClientService;

@Component("accessManager")
public class AccessManager {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ClientService clientService;
	
	public boolean isUser(Long id) {
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<User> result =  userRepository.findByEmail(email);
		
		if(!result.isPresent()) throw new NotFoundException("There are not user with email = " + email);
		
		User user = result.get();
		
		return user.getId() == id;
		
	}
	
	public boolean isClientUser(Long id) {
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<User> result =  userRepository.findByEmail(email);
		
		if(!result.isPresent()) throw new NotFoundException("There are not user with email = " + email);
		
		User user = result.get();
		
		Client client = clientService.getById(id);
		
		return user.getId() == (client.getUser().getId());
	}
	
	public boolean isNewClientUser(ClientSaveDto client) {
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<User> result =  userRepository.findByEmail(email);
		
		if(!result.isPresent()) throw new NotFoundException("There are not user with email = " + email);
		
		User user = result.get();
		
		return user.getId() == client.getUser().getId();
		
	}
	
}
