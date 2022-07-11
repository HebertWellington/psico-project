package com.psicoproject.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.psicoproject.domain.User;
import com.psicoproject.domain.enums.Role;
import com.psicoproject.exception.NotFoundException;
import com.psicoproject.model.PageModel;
import com.psicoproject.model.PageRequestModel;
import com.psicoproject.repository.UserRepository;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	private User userNew;

	@BeforeAll
	public void testLocação() {
		MockitoAnnotations.openMocks(this);
	}

	@Order(1)
	@Test
	public void deveRetornarNovoUsuarioComSenhaCriptografada() {

		// cenario
		User user = new User(null, "Test", "1234567", "test@gmail.com", Role.ADMINISTRATOR, null);
		user.setId(1L);
		this.userNew = user;
		Mockito.when(userRepository.save(user)).thenReturn(user);

		// acao
		user = userService.save(user);

		// verificacao
		assertThat(user.getPassword()).isNotEqualTo("1234567");
	}

	@Order(2)
	@Test
	public void deveRetornarUsuarioAtualizado() {

		// cenario
		User userUpdate = new User(null, "TestUpdate", "1234567", "test@gmail.com", Role.ADMINISTRATOR, null);
		userUpdate.setId(1L);
		this.userNew = userUpdate;
		Mockito.when(userRepository.save(userUpdate)).thenReturn(userUpdate);

		// acao
		userUpdate = userService.update(userUpdate);

		// verificacao
		assertThat(userUpdate.getUsername()).isEqualTo(userNew.getUsername());
	}

	@Test()
	public void deveRetornarNotFoundException() {

		// cenario
		Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());

		try {

		// acao
			userService.getById(2L);
			fail();

		} catch (NotFoundException e) {

		// verificacao
			assertThat(e.getMessage()).isEqualTo("User Not Foun With id = 2");
		}
	}

	@Test
	public void DeveRetornarUserDetails() {

		// cenario
		Optional<User> result = Optional.of(userNew);
		Mockito.when(userRepository.findByEmail(userNew.getEmail())).thenReturn(result);

		// acao
		UserDetails userDatails = userService.loadUserByUsername(userNew.getEmail());

		// verificacao
		assertThat(userDatails.getUsername()).isEqualTo(userNew.getEmail());
		assertThat(userDatails.getPassword()).isEqualTo(userNew.getPassword());
		assertThat(userDatails.getAuthorities().size()).isEqualTo(1);
	}

	@Test
	public void DeveRetornarListaUsuariosPageModel() {
		
		// cenario
		List<User> users = new ArrayList<>();
		users.add(userNew);
		PageRequestModel pr = new PageRequestModel(0, 10);
		Pageable pageable = PageRequest.of(pr.getPage(), pr.getSize());
		Page<User> page = new PageImpl<>(users, pageable, users.size());

		Mockito.when(userRepository.findAll(pageable)).thenReturn(page);

		// acao
		PageModel<User> pm = userService.listAllOnLazyMode(pr);

		// verificacao
		assertThat(pm.getTotalPages()).isEqualTo(1);
		assertThat(pm.getTotalElements()).isEqualTo(1);

	}

	@Test
	public void DeveRetornarUsernameNotFoundException() {

		// cenario
		User userNotFound = new User();
		Mockito.when(userRepository.findByEmail(userNotFound.getEmail())).thenReturn(Optional.empty());

		try {

		// acao
			userService.loadUserByUsername(userNotFound.getEmail());
			fail();

		} catch (UsernameNotFoundException e) {

		// verificacao
			assertThat(e.getMessage()).isEqualTo("Doesn't exist user");
		}
	}

}
