package org.example.ws.service;

import java.util.Collection;

import org.example.ws.model.Greeting;
import org.example.ws.model.GreetingDTO;

public interface GreetingDTOService {

	

	    /**
	     * Find all Greeting entities.
	     * @return A Collection of Greeting objects.
	     */
	    Collection<GreetingDTO> findAll();

	    /**
	     * Find a single Greeting entity by primary key identifier.
	     * @param id A Long primary key identifier.
	     * @return A Greeting or <code>null</code> if none found.
	     */
	    GreetingDTO findOne(Long id);

	    /**
	     * Persists a Greeting entity in the data store.
	     * @param greeting A Greeting object to be persisted.
	     * @return The persisted Greeting entity.
	     */
	    GreetingDTO create(GreetingDTO greeting);

	    /**
	     * Updates a previously persisted Greeting entity in the data store.
	     * @param greeting A Greeting object to be updated.
	     * @return The updated Greeting entity.
	     */
	    GreetingDTO update(GreetingDTO greeting);

	    /**
	     * Removes a previously persisted Greeting entity from the data store.
	     * @param id A Long primary key identifier.
	     */
	    void delete(Long id);
	
}
