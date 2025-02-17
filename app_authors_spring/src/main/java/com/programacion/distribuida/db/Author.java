package com.programacion.distribuida.db;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "authors")
@Data
public class Author {

    @Id
    private Integer id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;


}
