package com.psicoproject.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.psicoproject.domain.Client;
import com.psicoproject.domain.User;
import com.psicoproject.domain.enums.Role;
import com.psicoproject.dto.UserLoginDto;
import com.psicoproject.dto.UserSaveDto;
import com.psicoproject.dto.UserUpdateDto;
import com.psicoproject.security.AccessManager;
import com.psicoproject.service.UserService;
import com.psicoproject.service.util.HashUtil;

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
	private AuthenticationManager auth;

	@MockBean
	private AccessManager accessManager;

	private String token;

	private User user;

	@BeforeAll
	public void testLocação() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@Order(2)
	public void deveRetornarStatusCreated() throws JsonProcessingException, Exception {
		// cenario
		UserSaveDto userDto = new UserSaveDto("Test", "1234567", "test@gmail.com", Role.ADMINISTRATOR, null);
		User user = userDto.transformDto();
		user.setId(1L);
		Mockito.when(userService.save(user)).thenReturn(user);

		// acao
		mockMvc.perform(post("/users").contentType("application/json").content(objectMapper.writeValueAsString(userDto))
				.header("Authorization", "Bearer" + token))

				// verificacao
				.andExpect(status().isCreated());

	}

	@Test
	@Order(1)
	public void deveRetornarStatusOkComToken() throws JsonProcessingException, Exception {
		// cenario
		UserSaveDto userDtoSave = new UserSaveDto("Test", "1234567", "test@gmail.com", Role.ADMINISTRATOR, null);
		User user = userDtoSave.transformDto();
		String hash = HashUtil.securedHash(user.getPassword());
		user.setId(1L);
		user.setPassword(hash);
		UserLoginDto userDto = new UserLoginDto("1234567", user.getEmail());

		List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

		Mockito.when(auth.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), "1234567"))).thenReturn(
				new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(
						user.getEmail(), hash, true, true, true, true, authorities), hash, authorities));

		// acao
		MvcResult result = mockMvc
				.perform(post("/users/login").contentType("application/json")
						.content(objectMapper.writeValueAsString(userDto)))

				// verificacao
				.andExpect(status().isOk()).andReturn();

		token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");

		assertThat(token).isNotNull();

	}

	@Test
	public void deveRetornarStatusOkComValidacaoToken() throws JsonProcessingException, Exception {
		// cenario
		UserUpdateDto userDto = new UserUpdateDto("Test", "1234567", "testupdate@gmail.com", null);
		user = userDto.transformDto();
		user.setId(1L);
		List<Client> clients = new ArrayList<>();
		user.setClients(clients);
		Mockito.when(userService.update(user)).thenReturn(user);
		Mockito.when(accessManager.isUser(user.getId())).thenReturn(true);

		// acao
		MvcResult result = mockMvc
				.perform(put("/users/{id}", 1L).contentType("application/json")
						.content(objectMapper.writeValueAsString(userDto)).header("Authorization", "Bearer" + token))

				// verificacao
				.andExpect(status().isOk()).andReturn();
		String UserUpdate = JsonPath.read(result.getResponse().getContentAsString(), "$.email");
		assertThat(UserUpdate).isEqualTo("testupdate@gmail.com");
	}

	@Test
	public void deveRetornarStatusNaoAutorizado() throws JsonProcessingException, Exception {
		// cenario
		UserUpdateDto userDto = new UserUpdateDto("Test", "1234567", "testupdate@gmail.com", null);
		user = userDto.transformDto();
		user.setId(1L);
		List<Client> clients = new ArrayList<>();
		user.setClients(clients);
		Mockito.when(userService.update(user)).thenReturn(user);
		Mockito.when(accessManager.isUser(user.getId())).thenReturn(false);

		// acao
		MvcResult result = mockMvc
				.perform(put("/users/{id}", 1L).contentType("application/json")
						.content(objectMapper.writeValueAsString(userDto)).header("Authorization", "Bearer" + token))

				// verificacao
				.andExpect(status().isForbidden()).andReturn();
		String UserUpdate = JsonPath.read(result.getResponse().getContentAsString(), "$.msg");
		assertThat(UserUpdate).isEqualTo("Access is denied");
	}

}
