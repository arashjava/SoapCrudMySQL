package com.book.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.entity.Book;
import com.book.repository.BookRepository;

@Service
public class BookService implements IBookService {
	@Autowired
	private BookRepository bookRepository;

	@Override
	public List<Book> getAllBooks(){
		List<Book> list = new ArrayList<>();
		bookRepository.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public Book getBookById(long bookId) {
		Book obj = bookRepository.findByBookId(bookId);
		return obj;
//		return null;
	}
	@Override
	public boolean addBook(Book book) {
		   List<Book> list = bookRepository.findByTitle(book.getTitle()); 	
	       if (list.size() > 0) {
	    	   return false;
	       } else {
	    	   book = bookRepository.save(book);
	    	   return true;
	       }
	}
	@Override
	public void updateBook(Book book) {
		bookRepository.save(book);
		
	}
	@Override
	public void deleteBook(Book book) {
		bookRepository.delete(book);
		
	}
}
