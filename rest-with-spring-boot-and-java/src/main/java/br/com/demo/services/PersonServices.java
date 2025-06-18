package br.com.demo.services;

import br.com.demo.controller.PersonController;
import br.com.demo.data.dto.v1.PersonDTO;
import br.com.demo.exeptions.RequiredObjectIsNullExeption;
import br.com.demo.exeptions.ResourceNotFoundExeption;
import br.com.demo.mapper.DozerMapper;
import br.com.demo.model.Person;
import br.com.demo.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServices {
    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;

    public List<PersonDTO> findAll(){
        logger.info("Finding all people!");
        var persons = DozerMapper.parseListObjects(repository.findAll(), PersonDTO.class);

        persons.stream().forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));

        return persons;
    }

    public PersonDTO findById(Long id){
        logger.info("Finding one person!");
        var entity =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));

        PersonDTO vo = DozerMapper.parseObject(entity, PersonDTO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonDTO createPerson(PersonDTO person){
        if(person == null){
            throw new RequiredObjectIsNullExeption();
        }

        logger.info("Creating one person!");

        var entity = DozerMapper.parseObject(person, Person.class);

        var vo = DozerMapper.parseObject(repository.save(entity), PersonDTO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonDTO updatePerson(PersonDTO person){
        if(person == null){
            throw new RequiredObjectIsNullExeption();
        }

        logger.info("Updating one person!");

        var entity = repository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(repository.save(entity), PersonDTO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id){
        logger.info("Deleting one person!");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));
        repository.delete(entity);
    }
}
