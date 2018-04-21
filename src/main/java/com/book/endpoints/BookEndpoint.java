package com.book.endpoints;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.book.entity.Book;
import com.book.service.IBookService;

import tech.arash.ws.AddBookRequest;
import tech.arash.ws.AddBookResponse;
import tech.arash.ws.BookInfo;
import tech.arash.ws.DeleteBookRequest;
import tech.arash.ws.DeleteBookResponse;
import tech.arash.ws.GetAllBooksResponse;
import tech.arash.ws.GetBookByIdRequest;
import tech.arash.ws.GetBookByIdResponse;
import tech.arash.ws.ServiceStatus;
import tech.arash.ws.UpdateBookRequest;
import tech.arash.ws.UpdateBookResponse;

@Endpoint
public class BookEndpoint {
	private static final String NAMESPACE_URI = "http://arash.tech/book-ws";
	@Autowired
	private IBookService bookService;	

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBookByIdRequest")
	@ResponsePayload
	public GetBookByIdResponse getBook(@RequestPayload GetBookByIdRequest request) {
		GetBookByIdResponse response = new GetBookByIdResponse();
		BookInfo bookInfo = new BookInfo();
		BeanUtils.copyProperties(bookService.getBookById(request.getBookId()), bookInfo);
		response.setBookInfo(bookInfo);
		return response;
	}
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllBooksRequest")
	@ResponsePayload
	public GetAllBooksResponse getAllBooks() {
		GetAllBooksResponse response = new GetAllBooksResponse();
		List<BookInfo> bookInfoList = new ArrayList<>();
		List<Book> bookList = bookService.getAllBooks();
		for (int i = 0; i < bookList.size(); i++) {
			 BookInfo ob = new BookInfo();
		     BeanUtils.copyProperties(bookList.get(i), ob);
		     bookInfoList.add(ob);    
		}
		response.getBookInfo().addAll(bookInfoList);
		return response;
	}	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "addBookRequest")
	@ResponsePayload
	public AddBookResponse addBook(@RequestPayload AddBookRequest request) {
		AddBookResponse response = new AddBookResponse();		
    	ServiceStatus serviceStatus = new ServiceStatus();		
		Book book = new Book();
		book.setTitle(request.getTitle());
		book.setPrice(request.getPrice());		
        boolean flag = bookService.addBook(book);
        if (flag == false) {
        	serviceStatus.setStatusCode("CONFLICT");
        	serviceStatus.setMessage("Book is already registered...");
        	response.setServiceStatus(serviceStatus);
        } else {
			BookInfo bookInfo = new BookInfo();
	        BeanUtils.copyProperties(book, bookInfo);
			response.setBookInfo(bookInfo);
        	serviceStatus.setStatusCode("SUCCESS");
        	serviceStatus.setMessage("Book is added successfully...");
        	response.setServiceStatus(serviceStatus);
        }
        return response;
	}
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateBookRequest")
	@ResponsePayload
	public UpdateBookResponse updateBook(@RequestPayload UpdateBookRequest request) {
		Book book = new Book();
		BeanUtils.copyProperties(request.getBookInfo(), book);
		bookService.updateBook(book);
    	ServiceStatus serviceStatus = new ServiceStatus();
    	serviceStatus.setStatusCode("SUCCESS");
    	serviceStatus.setMessage("Book is updated successfully...");
    	UpdateBookResponse response = new UpdateBookResponse();
    	response.setServiceStatus(serviceStatus);
    	return response;
	}
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteBookRequest")
	@ResponsePayload
	public DeleteBookResponse deleteBook(@RequestPayload DeleteBookRequest request) {
		Book book = bookService.getBookById(request.getBookId());
    	ServiceStatus serviceStatus = new ServiceStatus();
		if (book == null ) {
	    	serviceStatus.setStatusCode("FAIL");
	    	serviceStatus.setMessage("Book is not registered, yet...");
		} else {
			bookService.deleteBook(book);
	    	serviceStatus.setStatusCode("SUCCESS");
	    	serviceStatus.setMessage("Book is deleted successfully...");
		}
    	DeleteBookResponse response = new DeleteBookResponse();
    	response.setServiceStatus(serviceStatus);
		return response;
	}	

}
