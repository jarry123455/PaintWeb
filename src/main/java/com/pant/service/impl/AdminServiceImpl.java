package com.pant.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.pant.model.Admin;
import com.pant.repository.AdminRepository;
import com.pant.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;
	
	@Override
	public Admin saveAdmin(Admin admin) {
		
		return adminRepository.save(admin);
	}

	@Override
	public List<Admin> getAllAdmin() {
		return adminRepository.findAll();
	}

	@Override
	public boolean deleteAdmin(int id) {
		
		Admin admin = adminRepository.findById(id).orElse(null);
		
		if (!ObjectUtils.isEmpty(admin)) {
			adminRepository.delete(admin);
			return true;
		}
		return false;
	}

	@Override
	public Admin getAdminById(int id) {
		Admin admin = adminRepository.findById(id).orElse(null);
		return admin;
	}

	@Override
	public Admin updateAdmin(Admin admin) {
		
		Admin updateAdmin = getAdminById(admin.getId());
		
		updateAdmin.setFullName(admin.getFullName());
		updateAdmin.setMobileNumber(admin.getMobileNumber());
		updateAdmin.setEmail(admin.getEmail());
		updateAdmin.setPassword(admin.getPassword());
		
		Admin admin2 = adminRepository.save(updateAdmin);
		
		if (!ObjectUtils.isEmpty(admin2)) {
			return admin;
		}
				
		return null;
	}

	@Override
	public boolean existsByEmail(String email) {
		
		return adminRepository.existsByEmail(email);
	}


	
	
	


}
