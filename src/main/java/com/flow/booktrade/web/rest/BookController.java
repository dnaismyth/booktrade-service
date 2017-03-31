package com.flow.booktrade.web.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	private static final String AUTHOR_PARAM = "author";
	private static final String TITLE_PARAM = "title";
	private static final String STATUS_PARAM = "status";
	
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
	 * Update the status of a book
	 * @param book
	 * @return
	 * @throws NoPermissionException
	 */
	@RequestMapping(value="/books/status", method = RequestMethod.PUT)
	@ResponseBody
	public Book updateBookAvailability(@RequestBody final Book book) throws NoPermissionException{
		User user = getCurrentUser();
		Book updated = bookService.updateBookAvailability(user, book);
		return updated;
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
	 * Search books
	 * @param page
	 * @param size
	 * @param criteria
	 * @return
	 */
	@RequestMapping(value="/books/search", method = RequestMethod.GET)
	@ResponseBody
	public Page<Book> searchForBooks(@RequestParam(value=PARAM_PAGE, required=true) int page, @RequestParam(value=PARAM_SIZE, required=true) int size,
			Map<String, String> criteria){
		User user = getCurrentUser();
		if(criteria.containsKey(AUTHOR_PARAM)){
			String author = criteria.get(AUTHOR_PARAM);
			Page<Book> results = bookService.searchBookByAuthor(author, new PageRequest(page, size));
			return results;
		}
		
		if(criteria.containsKey(TITLE_PARAM)){
			String title = criteria.get(TITLE_PARAM);
			Page<Book> results = bookService.searchBookByTitle(title, new PageRequest(page, size));
			return results;
		}
		
		return bookService.findMostRecentBooksInSameCity(user, new PageRequest(page, size));
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
	public RestResponse<Long> deleteBook(@PathVariable("bookId") Long bookId) throws NoPermissionException {
		User user = getCurrentUser();
		Long deletedId = bookService.removeBook(user, bookId);
		return new RestResponse<Long>(deletedId, OperationType.DELETE);
	}
	
	/**
	 * Find all books by userId
	 * @param userId
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/users/{userId}/books", method = RequestMethod.GET)
	@ResponseBody
	public Page<Book> findBooksByUserId(@PathVariable("userId") Long userId, @RequestParam(value=PARAM_PAGE, required=true) int page,
			@RequestParam(value=PARAM_SIZE, required=true) int size, @RequestParam(value=STATUS_PARAM, required=false) String status){
		if(status != null && status.equals("available")){
			Page<Book> availableBooks = bookService.findBooksAvailableByOwnerId(userId, new PageRequest(page, size));
			return availableBooks;
		} 
		
		Page<Book> unavailable = bookService.findUnavailableBooksByOwnerId(userId, new PageRequest(page,size));
		return unavailable;
	}

}
