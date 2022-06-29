package com.psicoproject.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.psicoproject.domain.User;
import com.psicoproject.domain.enums.Role;

@SpringBootTest
public class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;

	@Test
	public void saveTest() {
		User user = new User(null, "Hebert", "1234567", "hebertwellington@gmail.com", Role.ADMINISTRATOR, null);
		User userCreated = userRepository.save(user);

		assertThat(userCreated.getId()).isEqualTo(1L);

	}

	@Test
	public void updateUserTest() {
		User user = new User(1L, "Hebert Barros", "1234567", "hebertwellington@gmail.com", Role.ADMINISTRATOR, null);
		User userUpdate = userRepository.save(user);
		
		assertThat(userUpdate.getUsername()).isEqualTo("Hebert Barros");
 	}
	
	@Test
	public void getByIdUserTest() {
		Optional<User> result = userRepository.findById(1L);
		User user = result.get();
		
		assertThat(user.getPassword()).isEqualTo("1234567");
	}
	
	@Test
	public void listAllUserTest() {
		List<User> list = userRepository.findAll();
		
		assertThat(list.size()).isEqualTo(1);
		
	}
	
	@Test
	public void loginTest() {
		Optional<User> result = userRepository.login("hebertwellington@gmail.com", "1234567");
		User userLogin = result.get();
		
		assertThat(userLogin.getId()).isEqualTo(1L);
		
	}
	@Test
	public void updateRoleTest() {
		int affectedRows = userRepository.updateRole(1L, Role.SIMPLE);
		assertThat(affectedRows).isEqualTo(1);
	}
	
	
}
