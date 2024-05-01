package com.pant.config;


import java.util.Arrays;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.pant.model.Customer;

public class CustomUser implements UserDetails {
	
	private Customer customer;
	
	
	public CustomUser(Customer customer) {
		super();
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(customer.getRole());

		return Arrays.asList(authority);
	}

	@Override
	public String getPassword() {
		
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		
		return customer.getEmail();
	}
	
	

}
