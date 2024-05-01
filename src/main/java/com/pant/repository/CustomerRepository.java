package com.pant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pant.model.Customer;;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

	
	public boolean existsByEmail(String email); 
	
	public Customer findByEmail(String email);
	
	
}
 