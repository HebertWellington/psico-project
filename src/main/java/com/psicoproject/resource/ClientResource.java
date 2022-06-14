package com.psicoproject.resource;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.psicoproject.domain.Client;
import com.psicoproject.dto.ClientSaveDto;
import com.psicoproject.model.PageModel;
import com.psicoproject.model.PageRequestModel;
import com.psicoproject.service.ClientService;
import com.psicoproject.service.UserService;

@RestController
@RequestMapping(value = "clients")
public class ClientResource {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ClientService clientService;
	
	@PostMapping
	public ResponseEntity<Client> save(@RequestBody @Valid ClientSaveDto clientDto){
		Client client = clientDto.transformDto();
		Client clientCreated = clientService.save(client);
		return ResponseEntity.status(HttpStatus.CREATED).body(clientCreated);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Client> update(@PathVariable(value = "id") Long id, @RequestBody @Valid ClientSaveDto clientDto){
		Client client = clientDto.transformDto();
		Client clientConsulted = clientService.getById(id);
		Date date = clientConsulted.getCreationDate();
		client.setId(id);
		client.setCreationDate(date);
		Client clientUpdate = clientService.update(client);
		return ResponseEntity.ok(clientUpdate);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Client> getById(@PathVariable(value = "id") Long id){
		Client clientId = clientService.getById(id);
		return ResponseEntity.ok(clientId);
	}
	
	@GetMapping
	public ResponseEntity<PageModel<Client>> listAll(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10")int size){
		 PageRequestModel pr = new PageRequestModel(page, size);
		 PageModel<Client> pm = clientService.listAllOnLazyMode(pr);
		 return ResponseEntity.ok(pm);
	}
	
	
}
