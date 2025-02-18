package com.programacion.distribuida.books.rest;

import com.programacion.distribuida.books.clients.AuthorRestClient;
import com.programacion.distribuida.books.db.Book;
import com.programacion.distribuida.books.dto.AuthorDto;
import com.programacion.distribuida.books.dto.BookDto;
import com.programacion.distribuida.books.repo.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/books")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
@Transactional
public class BookRest {

    @Inject
    BookRepository repository;

    @Inject
    @RestClient
    AuthorRestClient authorRest;

    @GET
    public List<BookDto> findAll(){

        return  repository.streamAll()
                .map( book -> {
                    System.out.println("Buscando author con id: "+book.getAuthorId());
                    var author = authorRest.findById(book.getAuthorId());

                    var bookDto = new BookDto();

                    bookDto.setId(book.getId());
                    bookDto.setTitle(book.getTitle());
                    bookDto.setIsbn(book.getIsbn());
                    bookDto.setPrice(book.getPrice());
                    bookDto.setAuthorName(author.getFirstName()+ " " + author.getLastName());
                    return bookDto;
                })
                .toList();



    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Integer id){
        var obj = repository.findByIdOptional(id);

        if(obj.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var book = obj.get();
        var author = authorRest.findById(book.getAuthorId());

        var dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPrice(book.getPrice());
        dto.setAuthorName(author.getFirstName()+ " " + author.getLastName());

        return Response.ok(dto).build();
    }

    @POST
    public Response create(Book book){
        repository.persist(book);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Book book){
        var obj = repository.update(id, book);

        if(obj.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(obj).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id){
        var obj = repository.deleteById(id);
        if(!obj){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).build();
    }
}
