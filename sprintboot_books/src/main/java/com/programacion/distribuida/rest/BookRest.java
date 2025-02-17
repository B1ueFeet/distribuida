package com.programacion.distribuida.rest;

import com.programacion.distribuida.client.AuthorWebClient;
import com.programacion.distribuida.db.Book;
import com.programacion.distribuida.dto.AuthorDto;
import com.programacion.distribuida.dto.BookDto;
import com.programacion.distribuida.repo.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Transactional
public class BookRest {

    @Autowired
    private BookRepository bookRepository;


    private AuthorWebClient authorWebClient;

    @Autowired
    public BookRest(AuthorWebClient authorWebClient) {
        this.authorWebClient = authorWebClient;
    }

//    public BookRest(AuthorWebClient authorWebClient, BookRepository repository) {
//        this.authorWebClient = authorWebClient;
//        this.bookRepository = repository;
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Integer id) {

        var book = bookRepository.findById(id);

        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book.get());
    }

    @GetMapping
    public List<BookDto> getAll() {
        return bookRepository.findAll().stream()
                .map(book -> {
                    var bookDto = new BookDto();
                    bookDto.setId(book.getId());
                    bookDto.setTitle(book.getTitle());
                    bookDto.setIsbn(book.getIsbn());
                    bookDto.setPrice(book.getPrice());

                    var authorDto = authorWebClient.findAuthorById(book.getAuthorId());
                    bookDto.setAuthor(authorDto.getFirstName() + " " + authorDto.getLastName());

                    return bookDto;
                })
                .toList();
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Book book) {
        bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body("New book inserted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Integer id, @RequestBody Book book) {
        var actualizable = bookRepository.findById(id).get();
        if (actualizable == null) {
            return ResponseEntity.notFound().build();
        }
        actualizable.setPrice(book.getPrice());
        actualizable.setTitle(book.getTitle());
        actualizable.setIsbn(book.getIsbn());
        actualizable.setAuthorId(book.getAuthorId());

        bookRepository.save(actualizable);
        return ResponseEntity.ok(actualizable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        var book = bookRepository.findById(id);
        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        bookRepository.delete(book.get());
        return ResponseEntity.ok("Book "+id+" deleted");
    }
}
