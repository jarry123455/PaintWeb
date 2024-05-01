package com.pant.service;

import java.util.List;

import com.pant.model.Customer;

public interface CustomerService {
	
	public Customer saveCustomer(Customer customer);
	
	public boolean existsByEmail(String email);
	
	public List<Customer> getAllCustomers();
	
	public boolean deleteCustomer(int id);
	

}
