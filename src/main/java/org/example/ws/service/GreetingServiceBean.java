package org.example.ws.service;

import static java.util.stream.Collectors.toList;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import org.example.ws.model.Greeting;
import org.example.ws.model.GreetingDTO;
import org.example.ws.model.Greeting;
import org.example.ws.repository.GreetingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The GreetingServiceBean encapsulates all business behaviors operating on the
 * Greeting entity model object.
 * 
 * *
 */
@Service
@Transactional(
        propagation = Propagation.SUPPORTS,
        readOnly = true)
public class GreetingServiceBean implements GreetingService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The <code>CounterService</code> captures metrics for Spring Actuator.
     */
    @Autowired
    private CounterService counterService;

    /**
     * The Spring Data repository for Greeting entities.
     */
    @Autowired
    private GreetingRepository greetingRepository;

    @Override
    public Collection<Greeting> findAll() {
        logger.info("> findAll");

        counterService.increment("method.invoked.greetingServiceBean.findAll");

        Collection<Greeting> models = greetingRepository.findAll();

        logger.info("< findAll");
        return models;
    }

    @Override
    @Cacheable(
            value = "Greetings",
            key = "#id")
    public Greeting findOne(Long id) {
        logger.info("> findOne id:{}", id);

        counterService.increment("method.invoked.greetingServiceBean.findOne");

        Greeting Greeting = greetingRepository.findOne(id);
        

        logger.info("< findOne id:{}", id);
        return Greeting;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = "Greetings",
            key = "#result.id")
    public Greeting create(Greeting greeting) {
        logger.info("> create");

        counterService.increment("method.invoked.greetingServiceBean.create");

        // Ensure the entity object to be created does NOT exist in the
        // repository. Prevent the default behavior of save() which will update
        // an existing entity if the entity matching the supplied id exists.
        if (greeting.getId() != null) {
            // Cannot create Greeting with specified ID value
            logger.error(
                    "Attempted to create a Greeting, but id attribute was not null.");
            throw new EntityExistsException(
                    "The id attribute must be null to persist a new entity.");
        }

        Greeting savedGreeting = greetingRepository.save(greeting);

        logger.info("< create");
        return savedGreeting;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = "greetings",
            key = "#greeting.id")
    public Greeting update(Greeting greeting) {
        logger.info("> update id:{}", greeting.getId());

        counterService.increment("method.invoked.greetingServiceBean.update");

        // Ensure the entity object to be updated exists in the repository to
        // prevent the default behavior of save() which will persist a new
        // entity if the entity matching the id does not exist
        Greeting greetingToUpdate = findOne(greeting.getId());
        if (greetingToUpdate == null) {
            // Cannot update Greeting that hasn't been persisted
            logger.error(
                    "Attempted to update a Greeting, but the entity does not exist.");
            throw new NoResultException("Requested entity not found.");
        }

        greetingToUpdate.setText(greeting.getText());
        Greeting updatedGreeting = greetingRepository.save(greetingToUpdate);

        logger.info("< update id:{}", greeting.getId());
        return updatedGreeting;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CacheEvict(
            value = "greetings",
            key = "#id")
    public void delete(Long id) {
        logger.info("> delete id:{}", id);

        counterService.increment("method.invoked.greetingServiceBean.delete");

        greetingRepository.delete(id);

        logger.info("< delete id:{}", id);
    }

    @Override
    @CacheEvict(
            value = "greetings",
            allEntries = true)
    public void evictCache() {
        logger.info("> evictCache");
        logger.info("< evictCache");
    }
    
   public GreetingDTO greetingToDTO(Greeting greeting){
	   
	   GreetingDTO GreetingDTO = new GreetingDTO();
	   
	   GreetingDTO.setText(greeting.getText());
	   
	   return GreetingDTO;
	   
   }
   
   public Collection<GreetingDTO> ToDTOs(Collection<Greeting> models)
   
   {
	   return models.stream()
			   .map(this::greetingToDTO)
               .collect(toList());
	   
	   
   }
}
