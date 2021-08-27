package com.luv2code.springdemo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

	// Autowire the CustomerService (Dependency Injection)
	@Autowired
	private CustomerService customerService;
	
	// Add mapping for GET /customers
	@GetMapping("/customers") // Handling GET HTTP request for /customers
	public List<Customer> getCustomers() {
		
		// delegate the call to CustomerService
		return customerService.getCustomers();
	}
	
	
	// Get Single Customer
	// add mapping for GET /customers/{customerId}
	// This will return the customer based on id that we pass if it matched with the id column in our database..
	@GetMapping("/customers/{customerId}")
	public Customer getCustomer(@PathVariable int customerId) {
		
		// delegate the call to CustomerService
		Customer theCustomer = customerService.getCustomer(customerId);
		return theCustomer;
	}
}
