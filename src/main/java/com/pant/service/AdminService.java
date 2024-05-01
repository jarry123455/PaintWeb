package com.pant.service;

import java.util.List;

import com.pant.model.Admin;

public interface AdminService {

	public Admin saveAdmin(Admin admin);
	
	public List<Admin> getAllAdmin();
	
	public boolean deleteAdmin(int id);
	
	public Admin getAdminById(int id);
	
	public Admin updateAdmin(Admin admin);
	
	public boolean existsByEmail(String email);
	
	
}
