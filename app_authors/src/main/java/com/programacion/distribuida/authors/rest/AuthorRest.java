package com.programacion.distribuida.authors.rest;

import com.programacion.distribuida.authors.db.Author;
import com.programacion.distribuida.authors.repo.AuthorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/authors")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
//@Transactional
public class AuthorRest {

    @Inject
    private AuthorRepository repository;

    @Inject
    @ConfigProperty(name = "quarkus.http.port")
    Integer port;

    AtomicInteger counter = new AtomicInteger(1);

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtener Author",
            description = "Obtiene un author por Id.")
    public Response findById(@PathParam("id") Integer id) throws UnknownHostException {

        int value = counter.getAndIncrement();
        if(value%5 != 0) {
            String msg = String.format("Intento %d ==> error", value);
            System.out.println("*********** "+msg);
            throw new RuntimeException(msg);
        }

        System.out.printf("%s: Server %d\n", LocalDateTime.now(), port);

        var obj = repository.findById(id);

        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        String txt = String.format("[%s:%d]-%s", ipAddress,port, obj.getFirstName());

        var ret = new Author();
        ret.setId(obj.getId());
        ret.setFirstName(txt);
        ret.setLastName(obj.getLastName());

        return Response.ok(ret).build();
    }

    @GET
    @Operation(summary = "Obtener todos los Authors",
            description = "Obtiene la lista de todos los authors.")
    public List<Author> findAll(){
        return repository.findAll()
                .list();
    }

    @POST
    @Operation(summary = "Crear un nuevo Author",
            description = "Crea un nuevo author y guardarlo en la Base de Datos.")
    public Response create(Author author){
        repository.persist(author);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar un Author",
            description = "Actualiza un author existente por su Id.")
    public Response update(@PathParam("id") Integer id, Author author){
        var obj = repository.update(id, author);

        if(obj.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(obj).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminar un Author",
            description = "Eliminar un author existente por su Id.")
    public Response delete(@PathParam("id") Integer id){
        var obj = repository.deleteById(id);
        if(!obj){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).build();
    }


}
