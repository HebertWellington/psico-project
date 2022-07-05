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

import com.psicoproject.domain.Client;
import com.psicoproject.domain.User;
import com.psicoproject.domain.enums.Role;
import com.psicoproject.exception.NotFoundException;
import com.psicoproject.model.PageModel;
import com.psicoproject.model.PageRequestModel;
import com.psicoproject.repository.ClientRepository;
import com.psicoproject.repository.UserRepository;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class ClientServiceTest {

	@InjectMocks
	ClientService clientService;

	@Mock
	ClientRepository clientRepository;

	@Mock
	UserRepository userRepository;

	private User user;

	private Client clientNew;

	@BeforeAll
	public void testLocação() {
		MockitoAnnotations.openMocks(this);
	}

	@Order(1)
	@Test
	public void deveRetornarNovoClient() {

		//cenario

		user = new User(1L, "Test", "1234567", "test@gmail.com", Role.ADMINISTRATOR, null);

		Client client = new Client(1L, null, "ClientTest", 35, "Developer Java", "clienttest@gmail.com", "1198435500",
				"08/08/1987", "321.234.321.01", user);

		this.clientNew = client;

		Mockito.when(clientRepository.save(client)).thenReturn(client);

		//acao
		client = clientService.save(client);

		//verificacao
		assertThat(client.getCpf()).isEqualTo("321.234.321.01");
	}

	@Order(2)
	@Test
	public void deveRetornarClienteAtualizado() {

		//cenario
		Client clientUp = new Client(null, null, "ClientTest Test", null, null, null, null, null, null, null);
		clientNew.setName(clientUp.getName());

		Mockito.when(clientRepository.save(clientNew)).thenReturn(clientNew);

		//acao
		clientNew = clientService.update(clientNew);

		//verificacao
		assertThat(clientNew.getName()).isEqualTo("ClientTest Test");
		assertThat(clientNew.getId()).isEqualTo(1L);
	}

	@Test()
	public void deveRetornarNotFoundException() {
		//cenario
		Mockito.when(clientRepository.findById(2L)).thenReturn(Optional.empty());

		try {

		//acao
			clientService.getById(2L);
			fail();

		} catch (NotFoundException e) {

		//verificacao
			assertThat(e.getMessage()).isEqualTo("Client Not Found With id: 2");
		}
	}

	@Test
	public void deveRetornarListaClientesPageModelPorUserId() {
		// cenario
		List<Client> clients = new ArrayList<>();
		clients.add(clientNew);
		user.setClients(clients);
		Optional<User> result = Optional.of(user);
		PageRequestModel pr = new PageRequestModel(0, 10);
		Pageable pageable = PageRequest.of(pr.getPage(), pr.getSize());
		Page<Client> page = new PageImpl<>(user.getClients(), pageable, user.getClients().size());

		Mockito.when(userRepository.findById(user.getId())).thenReturn(result);
		Mockito.when(clientRepository.findAllByUserId(user.getId(), pageable)).thenReturn(page);

		// acao
		PageModel<Client> pm = clientService.listAllByUserIdOnLazyMode(clientNew.getUser().getId(), pr);

		// verificacao
		assertThat(pm.getTotalPages()).isEqualTo(1);
		assertThat(pm.getTotalElements()).isEqualTo(1);

	}

	@Test
	public void deveRetornarListaClientesPageModel() {
		// cenario
		List<Client> clients = new ArrayList<>();
		clients.add(clientNew);
		user.setClients(clients);
		PageRequestModel pr = new PageRequestModel(0, 10);
		Pageable pageable = PageRequest.of(pr.getPage(), pr.getSize());
		Page<Client> page = new PageImpl<>(user.getClients(), pageable, clients.size());

		Mockito.when(clientRepository.findAll(pageable)).thenReturn(page);

		// acao
		PageModel<Client> pm = clientService.listAllOnLazyMode(pr);

		// verificacao
		assertThat(pm.getTotalPages()).isEqualTo(1);
		assertThat(pm.getTotalElements()).isEqualTo(1);

	}

	@Test
	public void deveRetornarUmNotFoundException() {
		// cenario
		PageRequestModel pr = new PageRequestModel(0, 10);

		Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());

		try {
			
		// acao
			clientService.listAllByUserIdOnLazyMode(2L, pr);
			fail();
			
		//verificacao
		} catch (NotFoundException e) {
			assertThat(e.getMessage()).isEqualTo("Not Found id User: 2");
		}
	}
}
