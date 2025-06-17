package br.com.demo.services;

import br.com.demo.controller.BookController;
import br.com.demo.controller.PersonController;
import br.com.demo.data.vo.v1.BookVO;
import br.com.demo.data.vo.v1.PersonVO;
import br.com.demo.exeptions.RequiredObjectIsNullExeption;
import br.com.demo.exeptions.ResourceNotFoundExeption;
import br.com.demo.mapper.DozerMapper;
import br.com.demo.model.Book;
import br.com.demo.model.Person;
import br.com.demo.repositories.BookRepository;
import br.com.demo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {
    private Logger logger = Logger.getLogger(BookServices.class.getName());

    @Autowired
    private BookRepository repository;

    public List<BookVO> findAll(){
        logger.info("Finding all book!");
        var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);

        books.stream().forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));

        return books;
    }

    public BookVO findById(Long id){
        logger.info("Finding one book!");
        var entity =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));

        BookVO vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO person){
        if(person == null){
            throw new RequiredObjectIsNullExeption();
        }

        logger.info("Creating one book!");

        var entity = DozerMapper.parseObject(person, Book.class);

        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO person){
        if(person == null){
            throw new RequiredObjectIsNullExeption();
        }

        logger.info("Updating one book!");

        var entity = repository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));

        entity.setAuthor(person.getAuthor());
        entity.setLaunchDate(person.getLaunchDate());
        entity.setPrice(person.getPrice());
        entity.setTitle(person.getTitle());

        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id){
        logger.info("Deleting one book!");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));
        repository.delete(entity);
    }
}
