package org.example.ws.model;

import javax.persistence.Entity;

@Entity
public class Address extends TransactionalEntity {

	private String street, city, country;
	
}
