package com.programacion.distribuida.books.clients;

import com.programacion.distribuida.books.dto.AuthorDto;
//import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Service
@HttpExchange(url = "/authors")
public interface AuthorRestClient {

    @GetExchange("/{id}")
//    @Retry(name = "MyRetry", fallbackMethod = "findByIdFallback")
//    @CircuitBreaker(name = "app-authors", fallbackMethod = "findByIdFallback")
//    @Retry(name = "retryApi", fallbackMethod = "findByIdFallback")
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 2000))
    AuthorDto findById(@PathVariable("id") Integer id);

    @Recover
    default AuthorDto findByIdFallback(Integer id, Throwable t) {
        System.out.println("Fallback ejecutado debido a: " + t.getMessage());

        var dto = new AuthorDto();
        dto.setId(-1);
        dto.setFirstName("Autor no disponible");
        dto.setLastName("");

        return dto;
    }
}
