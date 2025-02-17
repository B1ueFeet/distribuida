package com.programacion.distribuida.repo;

import com.programacion.distribuida.db.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
