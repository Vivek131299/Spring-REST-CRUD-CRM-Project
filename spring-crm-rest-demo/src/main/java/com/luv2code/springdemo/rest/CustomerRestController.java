package com.luv2code.springdemo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	
	
	// Update Customer
	// add mapping for PUT /customers - update existing Customer
	// @PutMapping handles PUT request. (like @GetMapping for GET and @PostMapping for POST).
	@PutMapping("/customers")
	public Customer updateCustomer(@RequestBody Customer theCustomer) {
		
		// delegate the call to Service
		customerService.saveCustomer(theCustomer);
		// Since the customer id is set, DAO will update customer in the database.
		
		return theCustomer;
	}
	// We tested this method same as previous method in Postman software.
	// Just select PUT method.
	// Else all same as above.
	// BUT this time we also need to add id of the customer whose data we want to update, like-
	//	{
	//	    "id" : 1,
	//	    "firstName" : "Daniel",
	//	    "lastName" : "Vega",
	//	    "email" : "daniel.vega@luv2code.com"
	//	}
	// So, customer with id = 1 will be get updated with this new data.
	// And click SEND.
	// It will just echo the same updated data in response section. 
	
	
	
	// Delete Customer
	// add mapping for DELETE /customers/{customerId} - delete customer
	// @DeleteMapping handles DELETE Requests. (like @GetMapping for GET, @PostMapping for POST and @PutMapping for PUT).
	@DeleteMapping("/customers/{customerId}")
	public String deleteCustomer(@PathVariable int customerId) {
		
		// Check if customer of passed id (customerId) exists. If not, throw Exception
		Customer tempCustomer = customerService.getCustomer(customerId);
		// So this will return null if customer of that id (customerID) doesn't exist.
		
		// throw Exception if null
		if (tempCustomer == null) {
			throw new CustomerNotFoundException("Customer is not found - " + customerId);
		}
		// else, if customer is not null, means if customer exists then,
		// delegate the call to Service
		customerService.deleteCustomer(customerId);
		return "Deleted customer id - " + customerId;
	}
	// We also tested this DELETE Method in Postman software
	// For this we just need to select DELETE method in dropdown, 
	// then paste the link with /customerID like:
	// http://localhost:8080/spring-crm-rest/api/customers/1
	// So here customerId = 1, So it will delete customer of id = 1.
	// And finally it will give message that we passed in return statement above in the response section like-
	// Deleted customer id - 1
	
}
