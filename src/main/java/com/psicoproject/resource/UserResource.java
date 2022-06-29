package com.psicoproject.resource;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.psicoproject.dto.UserLoginResponseDto;
import com.psicoproject.dto.UserSaveDto;
import com.psicoproject.dto.UserUpdateDto;
import com.psicoproject.dto.UserUpdateRoleDto;
import com.psicoproject.model.PageModel;
import com.psicoproject.model.PageRequestModel;
import com.psicoproject.security.AccessManager;
import com.psicoproject.security.JwtManager;
import com.psicoproject.service.ClientService;
import com.psicoproject.service.UserService;

@RestController
@RequestMapping(value = "users")
public class UserResource {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtManager jwtManager;
	
	@Autowired
	private AccessManager accessManager;
	
	@Secured("ROLE_ADMINISTRATOR")
	@PostMapping
	public ResponseEntity<User> save(@RequestBody @Valid UserSaveDto userDto){
		User user = userDto.transformDto();
		User userCreated = userService.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
	}
	
	@PreAuthorize("@accessManager.isUser(#id)")
	@PutMapping(value = "/{id}")
	public ResponseEntity<User> update(@PathVariable(value = "id") Long id, @RequestBody @Valid UserUpdateDto userDto){
		User user = userDto.transformDto();
		user.setId(id);
		User userCreated = userService.update(user);
		return ResponseEntity.ok(userCreated);
	}
	
	@Secured("ROLE_ADMINISTRATOR")
	@GetMapping(value = "/{id}")
	public ResponseEntity<User> getById(@PathVariable(value = "id") Long id){
		User userId = userService.getById(id);
		return ResponseEntity.ok(userId);
	}
	
	@PostMapping("/login")
	public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginDto userDto){
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());
		Authentication auth = authManager.authenticate(token);
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		org.springframework.security.core.userdetails.User userDetail = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
		
		String email = userDetail.getUsername();
		List<String> role = auth.getAuthorities().stream().map(authorit -> authorit.getAuthority()).collect(Collectors.toList());
		
		UserLoginResponseDto jwt = jwtManager.createToken(email, role);
		
		return ResponseEntity.ok(jwt);
	}
	
	@Secured("ROLE_ADMINISTRATOR")
	@GetMapping
	public ResponseEntity<PageModel<User>> listAll(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10")int size){
		 PageRequestModel pr = new PageRequestModel(page, size);
		 PageModel<User> pm = userService.listAllOnLazyMode(pr);
		 return ResponseEntity.ok(pm);
	}
	
	@PreAuthorize("@accessManager.isUser(#id)")
	@GetMapping("/{id}/clients")
	public ResponseEntity<PageModel<Client>> listAllByUserId(@PathVariable(value = "id") Long id, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10")int size){
		PageRequestModel pr = new PageRequestModel(page, size);
		PageModel<Client> pm = clientService.listAllByUserIdOnLazyMode(id, pr);
		return ResponseEntity.ok(pm);
	}
	
	@Secured("ROLE_ADMINISTRATOR")
	@PatchMapping("/role/{id}")
	public ResponseEntity<?> updateRole(@PathVariable(value = "id") Long id, @RequestBody @Valid UserUpdateRoleDto roleDto){
		User user = new User();
		user.setId(id);
		user.setRole(roleDto.getRole());
		userService.updateRole(user);
		return ResponseEntity.ok().build();
	}
}
