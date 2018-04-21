package com.book.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.book.entity.Book;

public interface BookRepository extends CrudRepository<Book, Long>  {
	
	Book findByBookId(long bookId);

    List<Book> findByTitle(String title);
}
