package com.programacion.distribuida.books.repo;

import com.programacion.distribuida.books.db.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
