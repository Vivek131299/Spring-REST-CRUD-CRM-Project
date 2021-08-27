package com.luv2code.springdemo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	
	// Add mapping for POST /customers - add new customer
	@PostMapping("/customers")
	public Customer addCustomer(@RequestBody Customer theCustomer) {
	// Using @RequestBody Annotation, we can access the request body JSON data as a POJO.
		
		// also, just in case, the pass an id in JSON... set id to 0.
		// This is to force to save of new item... instead of update.
		// Because, as we are using Hibernate, it uses insertOrUpdate() method to insert
		// into database. If the id is null or 0, then it(DAO) inserts into the database, otherwise it updates.
		// So here we set id = 0 just in case if user pass an id into JSON.
		theCustomer.setId(0);
		
		
		// delegate the call to Service.
		customerService.saveCustomer(theCustomer);
		
		return theCustomer;
	}
	// We tested this POST method in Postman client software.
	// Set method to POST, paste URL- http://localhost:8080/spring-crm-rest/api/customers,
	// Select Body, select raw, and JSON from dropdown, 
	// Then write some json data for one customer to pass/add like- 
	//	{
	//    "firstName" : "Maria",
	//    "lastName" : "Gomez",
	//    "email" : "maria.gomez@luv2code.com"
	//	}
	// AND then click SEND.
	// And then we can see the response below with the new id for this customer.
	// So now finally we have this new customer added to our database.
	
}
