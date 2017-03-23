package com.flow.booktrade.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.flow.booktrade.dto.Book;
import com.flow.booktrade.dto.OperationType;
import com.flow.booktrade.dto.User;
import com.flow.booktrade.exception.NoPermissionException;
import com.flow.booktrade.exception.ResourceNotFoundException;
import com.flow.booktrade.service.BookService;
import com.flow.booktrade.web.rest.vm.RestResponse;

@RestController
@RequestMapping("/api")
public class BookController extends BaseController {
	
	@Autowired
	private BookService bookService;
	
	/**
	 * Find one book by it's id
	 * @param bookId
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value="/books/{bookId}", method = RequestMethod.GET)
	@ResponseBody
	public Book getBookById(@PathVariable("bookId") Long bookId) throws ResourceNotFoundException{
		return bookService.findBookById(bookId);
	}

	/**
	 * Create a new book for sale/trade
	 * @param book
	 * @return
	 */
	@RequestMapping(value="/books", method = RequestMethod.POST)
	@ResponseBody
	public Book createBook(@RequestBody  final Book book){
		User user = getCurrentUser();
		Book created = bookService.createBook(user, book);
		return created;
	}
	
	/**
	 * Update basic book properties
	 * @param bookId
	 * @param updated
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/books/{bookId}", method = RequestMethod.PUT)
	@ResponseBody
	public Book updateBook(@PathVariable("bookId") Long bookId, @RequestBody final Book updated) throws Exception{
		User user = getCurrentUser();
		updated.setId(bookId);
		Book book = bookService.updateBook(user, updated);
		return book;
	}
	
	/**
	 * Delete a book
	 * @param bookId
	 * @return
	 * @throws NoPermissionException
	 */
	@RequestMapping(value="/books/{bookId}", method = RequestMethod.DELETE)
	@ResponseBody
	public RestResponse<Long> deleteBook(@PathVariable("bookId") Long bookId) throws NoPermissionException{
		User user = getCurrentUser();
		Long deletedId = bookService.removeBook(user, bookId);
		return new RestResponse<Long>(deletedId, OperationType.DELETE);
	}

}
