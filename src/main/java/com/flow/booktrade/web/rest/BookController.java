package com.flow.booktrade.web.rest;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.flow.booktrade.domain.RBook;
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
	
    private final Logger log = LoggerFactory.getLogger(BookController.class);

	
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
	 * Filtered book search
	 * @param page
	 * @param size
	 * @param criteria
	 * @return
	 */
	@RequestMapping(value="/books/search/filter", method = RequestMethod.GET)
	@ResponseBody
	public Page<Book> filterSearchForBooks(@RequestParam(value=PARAM_PAGE, required=true) int page, @RequestParam(value=PARAM_SIZE, required=true) int size,
			@RequestParam Map<String, String> criteria){
		User user = getCurrentUser();
		Page<Book> books = bookService.filterBookSearch(new PageRequest(page, size), criteria, user);
		return books;
	}
	
	/**
	 * Return most recent books in same city
	 * If a user does not have a city, provide most recent books
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/books/recent", method = RequestMethod.GET)
	public Page<Book> getMostRecentBooks(@RequestParam(value=PARAM_PAGE, required=true) int page, @RequestParam(value=PARAM_SIZE, required=true) int size){
		User user = getCurrentUser();
		return bookService.findMostRecentBooksInSameCity(user, new PageRequest(page, size));
	}
	
	
	/**
	 * Update basic book properties
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
