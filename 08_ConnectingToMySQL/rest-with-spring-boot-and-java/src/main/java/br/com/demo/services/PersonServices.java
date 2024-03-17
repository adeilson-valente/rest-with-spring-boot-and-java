package br.com.demo.services;

import br.com.demo.exeptions.ResourceNotFoundExeption;
import br.com.demo.model.Person;
import br.com.demo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;

    public List<Person> findAll(){
        logger.info("Finding all people!");
        return repository.findAll();
    }

    public Person findById(Long id){
        logger.info("Finding one person!");
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));
    }

    public Person createPerson(Person person){
        logger.info("Creating one person!");

        return repository.save(person);
    }

    public Person updatePerson(Person person){
        logger.info("Updating one person!");

        var entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));

        entity.setFistName(person.getFistName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return repository.save(entity);
    }

    public void delete(Long id){
        logger.info("Deleting one person!");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));
        repository.delete(entity);
    }
}
