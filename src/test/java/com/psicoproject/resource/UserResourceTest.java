package com.psicoproject.resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psicoproject.domain.User;
import com.psicoproject.domain.enums.Role;
import com.psicoproject.dto.UserLoginDto;
import com.psicoproject.dto.UserSaveDto;
import com.psicoproject.repository.UserRepository;
import com.psicoproject.service.UserService;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
public class UserResourceTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@InjectMocks
	private UserResource userResource;

	@MockBean
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	private User userNew;

	@BeforeAll
	public void testLocação() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void criarUsuario() throws JsonProcessingException, Exception {
		// cenario
		UserSaveDto userDto = new UserSaveDto("Test", "1234567", "test@gmail.com", Role.ADMINISTRATOR, null);
		User user = userDto.transformDto();
		user.setId(1L);
		this.userNew = user;
		Optional<User> result = Optional.of(user);
		Mockito.when(userService.save(user)).thenReturn(user);
		Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(result);

		// acao
		mockMvc.perform(post("/users").contentType("application/json").content(objectMapper.writeValueAsString(userDto))
				.header("Authorization",
						"BearereyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImV4cCI6MTY1NzQ5MTMzOCwiUm9sZSI6WyJST0xFX0FETUlOSVNUUkFUT1IiXX0.uo7xjEVwneKsy8l8nJyT5cOEy36J4MEfuqOhj6rtXmQwVNoIHHjrshZyqMyu-kGJqP3sN2wDux_gXy3o11JjPw"))

				// verificacao
				.andExpect(status().isCreated());

	}

}
