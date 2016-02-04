package org.example.ws.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

public class Customer extends TransactionalEntity  {
	
	private String firstname, lastname;
	
	@Column(unique = true)
	private EmailAddress emailAddress;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "customer_id")
	private Set<Address> addresses;
}
