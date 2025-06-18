package br.com.demo.services;

import br.com.demo.controller.BookController;
import br.com.demo.controller.PersonController;
import br.com.demo.data.dto.v1.BookDTO;
import br.com.demo.exeptions.RequiredObjectIsNullExeption;
import br.com.demo.exeptions.ResourceNotFoundExeption;
import br.com.demo.mapper.DozerMapper;
import br.com.demo.model.Book;
import br.com.demo.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {
    private Logger logger = LoggerFactory.getLogger(BookServices.class.getName());

    @Autowired
    private BookRepository repository;

    public List<BookDTO> findAll(){
        logger.info("Finding all book!");
        var books = DozerMapper.parseListObjects(repository.findAll(), BookDTO.class);

        books.stream().forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));

        return books;
    }

    public BookDTO findById(Long id){
        logger.info("Finding one book!");
        var entity =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));

        BookDTO dto = DozerMapper.parseObject(entity, BookDTO.class);
        dto.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return dto;
    }

    public BookDTO create(BookDTO person){
        if(person == null){
            throw new RequiredObjectIsNullExeption();
        }

        logger.info("Creating one book!");

        var entity = DozerMapper.parseObject(person, Book.class);

        var dto = DozerMapper.parseObject(repository.save(entity), BookDTO.class);
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public BookDTO update(BookDTO person){
        if(person == null){
            throw new RequiredObjectIsNullExeption();
        }

        logger.info("Updating one book!");

        var entity = repository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));

        entity.setAuthor(person.getAuthor());
        entity.setLaunchDate(person.getLaunchDate());
        entity.setPrice(person.getPrice());
        entity.setTitle(person.getTitle());

        var dto = DozerMapper.parseObject(repository.save(entity), BookDTO.class);
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public void delete(Long id){
        logger.info("Deleting one book!");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));
        repository.delete(entity);
    }
}
