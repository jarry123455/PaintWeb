package com.pant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pant.model.Admin;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
	
	public Admin findByEmail(String email);
	
	public boolean existsByEmail(String email);

}
