package com.psicoproject.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.psicoproject.domain.Client;
import com.psicoproject.domain.User;
import com.psicoproject.domain.enums.Role;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class ClientRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ClientRepository clientRepository;

	private User user;

	private Client client;

	private Long list;

	private List<Client> listUp;

	@BeforeAll
	public void setup() {
		Long list = clientRepository.count();
		this.list = list;
		
		User user = new User(null, "Teste", "1234567", "teste@gmail.com", Role.ADMINISTRATOR, null);
		userRepository.save(user);
		this.user = user;
		
		List<Client> listUserId = clientRepository.findAllByUserId(user.getId());
		this.listUp = listUserId;
		
	}
	@Order(1)
	@Test
	public void salvarNovoClienteNoBancoDeDados() {
		Client client = new Client(null, new Date(), "ClientTest", 35, "Developer Java", "clienttest@gmail.com", "1198435500",
				"08/08/1987", "321.234.321.01", user);
		Client clientCreated = clientRepository.save(client);
		this.client = clientCreated;
		
		assertThat(clientCreated.getCpf()).isEqualTo("321.234.321.01");

	}

	@Order(2)
	@Test
	public void atualizarClienteNBancoDeDados() {
		Optional<Client> result = clientRepository.findById(client.getId());
		Client clientUpdate = result.get();

		if (result.isPresent()) {
			clientUpdate.setName("ClientTest Updated");
			clientUpdate.setCreationDate(new Date());
			clientRepository.save(clientUpdate);
		}

		assertThat(clientUpdate.getName()).isEqualTo("ClientTest Updated");
	}

	@Test
	public void deveRetornarClientePorIdNoBancoDeDados() {
		Optional<Client> result = clientRepository.findById(client.getId());
		Client client = result.get();

		assertThat(client.getAge()).isEqualTo(35);
	}

	@Test
	public void deveRetornarListaClientsDoBancoDeDados() {
		Long listUpdate = clientRepository.count();

		assertThat(listUpdate).isEqualTo(list + 1L);

	}

	@Test
	public void deveRetornarListaClientesPorUserIdDoBancoDeDados() {
		List<Client> listUpdate = clientRepository.findAllByUserId(user.getId());

		assertThat(listUpdate.size()).isEqualTo(listUp.size() + 1);

	}
	
	@AfterAll
	public void deleteRows() {
		clientRepository.deleteById(client.getId());
		userRepository.deleteById(user.getId());
	}
}
