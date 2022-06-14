package com.psicoproject.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
import com.psicoproject.domain.User;
import com.psicoproject.dto.UserLoginDto;
import com.psicoproject.dto.UserSaveDto;
import com.psicoproject.model.PageModel;
import com.psicoproject.model.PageRequestModel;
import com.psicoproject.service.ClientService;
import com.psicoproject.service.UserService;

@RestController
@RequestMapping(value = "users")
public class UserResource {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ClientService clientService;
	
	@PostMapping
	public ResponseEntity<User> save(@RequestBody @Valid UserSaveDto userDto){
		User user = userDto.transformDto();
		User userCreated = userService.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<User> update(@PathVariable(value = "id") Long id, @RequestBody @Valid UserSaveDto userDto){
		User user = userDto.transformDto();
		user.setId(id);
		User userCreated = userService.update(user);
		return ResponseEntity.ok(userCreated);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<User> getById(@PathVariable(value = "id") Long id){
		User userId = userService.getById(id);
		return ResponseEntity.ok(userId);
	}
	
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody @Valid UserLoginDto userDto){
		User user = userService.login(userDto.getEmail(), userDto.getPassword());
		return ResponseEntity.ok(user);
	}
	
	@GetMapping
	public ResponseEntity<PageModel<User>> listAll(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10")int size){
		 PageRequestModel pr = new PageRequestModel(page, size);
		 PageModel<User> pm = userService.listAllOnLazyMode(pr);
		 return ResponseEntity.ok(pm);
	}
	
	@GetMapping("/{id}/clients")
	public ResponseEntity<PageModel<Client>> listAllByUserId(@PathVariable(value = "id") Long id, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10")int size){
		PageRequestModel pr = new PageRequestModel(page, size);
		PageModel<Client> pm = clientService.listAllByUserIdOnLazyMode(id, pr);
		return ResponseEntity.ok(pm);
	}
}
