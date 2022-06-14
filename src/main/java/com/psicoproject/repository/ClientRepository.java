package com.psicoproject.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.psicoproject.domain.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
	
	public List<Client> findAllByUserId(Long id);

	public Page<Client> findAllByUserId(Long id, Pageable pageable);

}
