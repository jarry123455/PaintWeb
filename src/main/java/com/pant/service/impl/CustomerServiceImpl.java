package com.pant.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.pant.model.Customer;
import com.pant.repository.CustomerRepository;
import com.pant.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public Customer saveCustomer(Customer customer) {
		
		String password = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(password);		
		customer.setRole("ROLE_USER");
		Customer save = customerRepository.save(customer);
		
		return save;
	}
	
/**	@Override
	public User saveUser(User user) {

		String password=passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		user.setRole("ROLE_USER");
		User newuser = userRepo.save(user);

		return newuser;
	}**/

	@Override
	public boolean existsByEmail(String email) {
		// TODO Auto-generated method stub
		return customerRepository.existsByEmail(email);
	}

	@Override
	public List<Customer> getAllCustomers() {
		
		return customerRepository.findAll();
	}

	@Override
	public boolean deleteCustomer(int id) {
		Customer customer = customerRepository.findById(id).orElse(null);
		
		if (!ObjectUtils.isEmpty(customer)) {
			customerRepository.delete(customer);
			return true;
		}
		return false;
	}

	

	
}
