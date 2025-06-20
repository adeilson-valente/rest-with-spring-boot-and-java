package br.com.demo.services;

import br.com.demo.api.v1.controller.BookController;
import br.com.demo.api.v1.controller.PersonController;
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

        books.forEach(this::addHateoasLinks);

        return books;
    }

    public BookDTO findById(Long id){
        logger.info("Finding one book!");
        var entity =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));

        BookDTO dto = DozerMapper.parseObject(entity, BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO create(BookDTO book){
        if(book == null){
            throw new RequiredObjectIsNullExeption();
        }

        logger.info("Creating one book!");

        var entity = DozerMapper.parseObject(book, Book.class);

        var dto = DozerMapper.parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO update(BookDTO book){
        if(book == null){
            throw new RequiredObjectIsNullExeption();
        }

        logger.info("Updating one book!");

        var entity = repository.findById(book.getKey()).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        var dto = DozerMapper.parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id){
        logger.info("Deleting one book!");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption(" No records found for this ID!"));
        repository.delete(entity);
    }

    private void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getKey())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getKey())).withRel("delete").withType("DELETE"));
    }
}
