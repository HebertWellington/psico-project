package com.psicoproject.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.psicoproject.domain.Client;
import com.psicoproject.domain.User;
import com.psicoproject.domain.enums.Role;

@SpringBootTest
public class ClientRepositoryTest {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ClientRepository clientRepository;

	@Test
	public void saveTest() {
		User user = new User(null, "Hebert", "1234567", "hebertwellington@gmail.com", Role.ADMINISTRATOR, null);
		userRepository.save(user);
		
		Client client = new Client(null, new Date(), "Douglas", 35, "Developer Java", "doug@gmail.com", "1198435500", "08/08/1987", "321.234.321.01", user);  
		Client clientCreated = clientRepository.save(client);
		
		assertThat(clientCreated.getId()).isEqualTo(1L);

	}

	@Test
	public void updateClientTest() {
		Optional<Client> result = clientRepository.findById(1L);
		Client clientUpdate = result.get();

		if(result.isPresent()) {
			clientUpdate.setName("Douglas Barros");
			clientUpdate.setCreationDate(new Date());
			clientRepository.save(clientUpdate);
		}
		
		assertThat(clientUpdate.getName()).isEqualTo("Douglas Barros");
 	}
	
	@Test
	public void getByIdClientTest() {
		Optional<Client> result = clientRepository.findById(1L);
		Client client = result.get();
		
		assertThat(client.getAge()).isEqualTo(35);
	}
	
	@Test
	public void listAllClientTest() {
		List<Client> list = clientRepository.findAll();
		
		assertThat(list.size()).isEqualTo(1);
		
	}
	
	@Test
	public void listByUserIdTest() {
		List<Client> list = clientRepository.findAllByUserId(1L);
		
		assertThat(list.size()).isEqualTo(1);

	}
}
