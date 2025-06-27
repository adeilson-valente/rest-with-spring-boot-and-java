package br.com.demo.unitTests.mockito.services;

import br.com.demo.data.dto.BookDTO;
import br.com.demo.exeptions.RequiredObjectIsNullExeption;
import br.com.demo.model.Book;
import br.com.demo.repositories.BookRepository;
import br.com.demo.services.BookService;
import br.com.demo.unitTests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookServicesTest {
    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    BookRepository repository;

    @Mock
    PagedResourcesAssembler<BookDTO> assembler;

    @BeforeEach
    void setUpMocks() throws Exception {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links"));
        assertEquals("Some Author1", result.getAuthor());
        assertEquals("Some Title1", result.getTitle());
        assertEquals(25D, result.getPrice());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void findAll() {
        // Mocking repository access
        List<Book> mockEntityList = input.mockEntityList();
        Page<Book> mockPage = new PageImpl<>(mockEntityList);
        when(repository.findAll(any(Pageable.class))).thenReturn(mockPage);

        List<BookDTO> mockDtoList = input.mockDTOList();

        // Mocking assembler
        // assembler.toModel(booksWithLinks, findAllLink);
        List<EntityModel<BookDTO>> entityModels = mockDtoList.stream()
                .map(EntityModel::of)
                .collect(Collectors.toList());

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                mockPage.getSize(),
                mockPage.getNumber(),
                mockPage.getTotalElements(),
                mockPage.getTotalPages()
        );

        PagedModel<EntityModel<BookDTO>> mockPagedModel = PagedModel.of(entityModels, pageMetadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(mockPagedModel);


        // Executing fid all
        PagedModel<EntityModel<BookDTO>> result = service.findAll(PageRequest.of(0, 14));

        List<BookDTO> books = result.getContent()
                .stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList());

        assertNotNull(books);
        assertEquals(14, books.size());

        validateIndividualBook(books.get(1), 1);
        validateIndividualBook(books.get(4), 4);
        validateIndividualBook(books.get(7), 7);
    }

    @Test
    void testCreateBook() {
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.save(book)).thenReturn(persisted);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links"));
        assertEquals("Some Author1", result.getAuthor());
        assertEquals("Some Title1", result.getTitle());
        assertEquals(25D, result.getPrice());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void testUpdatePerson() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        Book persisted = entity;

        BookDTO vo = input.mockDTO(1);
        vo.setKey(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        var result = service.update(vo);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links"));
        assertEquals("Some Author1", result.getAuthor());
        assertEquals("Some Title1", result.getTitle());
        assertEquals(25D, result.getPrice());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void testDelete() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullExeption.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullExeption.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private static void validateIndividualBook(BookDTO book, int i) {
        assertNotNull(book);
        assertNotNull(book.getKey());
        assertNotNull(book.getLinks());

        assertNotNull(book.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/book/v1/" + i)
                        && link.getType().equals("GET")
                ));

        assertNotNull(book.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(book.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(book.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(book.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/book/v1/" + i)
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Some Author" + i, book.getAuthor());
        assertEquals(25D, book.getPrice());
        assertEquals("Some Title" + i, book.getTitle());
        assertNotNull(book.getLaunchDate());
    }
}
