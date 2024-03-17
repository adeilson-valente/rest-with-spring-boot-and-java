package br.com.demo.services;

import br.com.demo.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private final AtomicLong counter = new AtomicLong();

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public List<Person> findAll(){
        logger.info("Finding all people!");

        List<Person> persons = new ArrayList<>();

        for (int i = 0; i<8; i++){
            Person person = mockPerson(i);
            persons.add(person);
        }

        return persons;
    }

    public Person findById(String id){
        logger.info("Finding one person!");
        Person person =  new Person();

        person.setId(counter.incrementAndGet());
        person.setFistName("Adeilson");
        person.setLastName("Valente");
        person.setAddress("Campos Belos - GO");
        person.setGender("Male");

        return person;
    }

    public Person createPerson(Person person){
        logger.info("Creating one person!");

        return person;
    }

    public Person updatePerson(Person person){
        logger.info("Updating one person!");
        person.setId(counter.incrementAndGet());
        return person;
    }

    public void delete(String id){
        logger.info("Deleting one person!");
    }

    private Person mockPerson(int i) {
        Person person =  new Person();

        person.setId(counter.incrementAndGet());
        person.setFistName("Person name " + i);
        person.setLastName("Last name " + i);
        person.setAddress("Some Address in Brasil " + i);
        person.setGender("Male");

        return person;
    }
}
