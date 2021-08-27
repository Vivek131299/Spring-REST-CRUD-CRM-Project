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
		
		// Now, there is actually an issue with this, like if we pass the Path Variable which is
		// not in the database or out of bound,, then getCustomer() method will return null.
		// AND if we pass any characters instead of integers, then it will throw an Exception.
		//
		// So, to SOLVE this, we will define our own custom Exception (CustomerNotFoundException).
		// So, we will have a Global Exception Handler class / ControllerAdvice (CustomerRestExceptionHandler).
		if (theCustomer == null) {
			throw new CustomerNotFoundException("Customer is not found - " + customerId);
		}
		
		return theCustomer;
	}
	
}
