package br.com.demo.services;

import br.com.demo.data.vo.v1.PersonVO;
import br.com.demo.data.vo.v2.PersonVOV2;
import br.com.demo.exeptions.ResourceNotFoundExeption;
import br.com.demo.mapper.DozerMapper;
import br.com.demo.mapper.custom.PersonMapper;
import br.com.demo.model.Person;
import br.com.demo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonMapper personMapper;

    public List<PersonVO> findAll(){
        logger.info("Finding all people!");
        return DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
    }

    public PersonVO findById(Long id){
        logger.info("Finding one person!");
        var entity =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));

        return DozerMapper.parseObject(entity, PersonVO.class);
    }

    public PersonVO createPerson(PersonVO person){
        logger.info("Creating one person!");

        var entity = DozerMapper.parseObject(person, Person.class);

        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        return vo;
    }

    public PersonVOV2 createPersonV2(PersonVOV2 person){
        logger.info("Creating one person with V2!");

        var entity = personMapper.convertVOToEntity(person);

        var vo = personMapper.convertEntityToVO(repository.save(entity));
        return vo;
    }

    public PersonVO updatePerson(PersonVO person){
        logger.info("Updating one person!");

        var entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        return vo;
    }

    public void delete(Long id){
        logger.info("Deleting one person!");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));
        repository.delete(entity);
    }
}
