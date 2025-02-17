package com.programacion.distribuida.repo;

import com.programacion.distribuida.db.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepo extends JpaRepository<Author, Integer> {
}
