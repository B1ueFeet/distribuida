package com.programacion.distribuida.client;


import com.programacion.distribuida.dto.AuthorDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthorWebClient {

    private final RestTemplate restTemplate;

    AuthorWebClient(@LoadBalanced RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public AuthorDto findAuthorById(Integer id) {
        return restTemplate.getForObject("http://app-authors/authors/{id}", AuthorDto.class, id);
    }

}
