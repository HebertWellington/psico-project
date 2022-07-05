package com.psicoproject.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.psicoproject.domain.User;
import com.psicoproject.domain.enums.Role;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	private Long list;

	private Long resultUpdate;

	private User userNew;

	@BeforeAll
	public void setup() {
		Long list = userRepository.count();
		this.list = list;

	}

	@Test
	@Order(1)
	public void salvarNovoUsuarioNoBancoDeDados() {
		User user = new User(null, "Test", "1234567", "test@gmail.com", Role.ADMINISTRATOR, null);
		this.userNew = user;
		User userCreated = userRepository.save(userNew);

		assertThat(userCreated.getEmail()).isEqualTo("test@gmail.com");

	}

	@Test
	@Order(2)
	public void atualizarUsuarioNoBancoDeDados() {
		Optional<User> result = userRepository.findByEmail("test@gmail.com");
		this.resultUpdate = result.get().getId();
		User userUp = new User(result.get().getId(), "TestUpdated", "1234567", "test@gmail.com", Role.ADMINISTRATOR, null);
		User userUpdate = userRepository.save(userUp);

		assertThat(userUpdate.getUsername()).isEqualTo("TestUpdated");
	}

	@Test
	public void deveRetornarUsuarioPorIdDoBancoDeDados() {
		Optional<User> resultId = userRepository.findById(resultUpdate);
		User user = resultId.get();

		assertThat(user.getPassword()).isEqualTo("1234567");
	}

	@Test
	public void deveRetornarListaUsuariosNoBancoDeDados() {
		Long listUpdated = userRepository.count();
		
		assertThat(listUpdated).isEqualTo(list + 1L);
	}

	@Test
	public void deveRetornarUsuarioPorEmailSenhaDoBancoDeDados() {
		Optional<User> resultLogin = userRepository.login("test@gmail.com", "1234567");
		User userLogin = resultLogin.get();

		assertThat(userLogin.getId()).isEqualTo(resultUpdate);

	}

	@Test
	public void deveRetornarUsuarioComRoleArtalizadoDoBancoDeDados() {
		int affectedRows = userRepository.updateRole(resultUpdate, Role.SIMPLE);
		
		assertThat(affectedRows).isEqualTo(1);
	}

	@AfterAll
	public void deleteRows() {
		userRepository.deleteById(resultUpdate);
	}

}
