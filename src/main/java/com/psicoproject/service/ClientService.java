package com.psicoproject.service;


import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.psicoproject.domain.Client;
import com.psicoproject.domain.User;
import com.psicoproject.exception.NotFoundException;
import com.psicoproject.model.PageModel;
import com.psicoproject.model.PageRequestModel;
import com.psicoproject.repository.ClientRepository;
import com.psicoproject.repository.UserRepository;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Client save(Client client) {
		client.setCreationDate(new Date());
				
		Client createdClient = clientRepository.save(client);
		
		return createdClient;
	}
	
	public Client update(Client client) {
		Client clientUpdated = clientRepository.save(client);
		
		return clientUpdated;
	}
	
	public Client getById(Long id) {
		Optional<Client> result = clientRepository.findById(id);
		
		return result.orElseThrow((() -> new NotFoundException("Client Not Found With id: "+id)));
	}
	
	public PageModel<Client> listAllByUserIdOnLazyMode(Long id, PageRequestModel pr) throws NotFoundException{
		Optional<User> result =userRepository.findById(id);
		if(!result.isPresent()) throw  new NotFoundException("Not Found id User: "+id);
		
		Pageable pageable = PageRequest.of(pr.getPage(), pr.getSize());
		Page<Client> page = clientRepository.findAllByUserId(id, pageable);
				
		PageModel<Client> pm = new PageModel<>((int)page.getTotalElements(), page.getSize(), page.getTotalPages(), page.getContent());
		
		return pm;
	}
	
	public PageModel<Client> listAllOnLazyMode(PageRequestModel pr){
		Pageable pageable = PageRequest.of(pr.getPage(), pr.getSize());
		Page<Client> page = clientRepository.findAll(pageable);
		
		PageModel<Client> pm = new PageModel<>((int)page.getTotalElements(), page.getSize(), page.getTotalPages(), page.getContent());
		
		return pm;
	}
	
	
}
