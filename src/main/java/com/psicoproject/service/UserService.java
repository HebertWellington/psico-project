package com.psicoproject.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.psicoproject.domain.User;
import com.psicoproject.exception.NotFoundException;
import com.psicoproject.model.PageModel;
import com.psicoproject.model.PageRequestModel;
import com.psicoproject.repository.UserRepository;
import com.psicoproject.service.util.HashUtil;

@Service
public class UserService {
	

	@Autowired
	private UserRepository userRepository;

	public User save(User user) {
		String hash = HashUtil.securedHash(user.getPassword());
		user.setPassword(hash);

		User createdUser = userRepository.save(user);
		return createdUser;
	}
	
	public User update(User user) {
		String hash = HashUtil.securedHash(user.getPassword());
		user.setPassword(hash);
		
		User updateUser = userRepository.save(user);
		return updateUser;
	}
	
	public User getById(Long id) {
		Optional<User> result = userRepository.findById(id);
		
		return result.orElseThrow(() -> new NotFoundException("User Not Foun With id = "+id));
	}
	
	public List<User> listAll(){
		List<User> users = userRepository.findAll();
		
		return users;
	}
	
	public User login(String email, String password) {
		password = HashUtil.securedHash(password);
		Optional<User> result = userRepository.login(email, password);
		return result.orElseThrow(() -> new NotFoundException("User Not Found"));
	}
	
	public PageModel<User> listAllOnLazyMode(PageRequestModel pr){
		Pageable pageable = PageRequest.of(pr.getPage(), pr.getSize());
		Page<User> page = userRepository.findAll(pageable);
		
		PageModel<User> pm = new PageModel<>((int)page.getTotalElements(), page.getSize(), page.getTotalPages(), page.getContent());
		
		return pm;
	}
}
