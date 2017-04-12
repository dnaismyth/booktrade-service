package com.flow.booktrade.service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.flow.booktrade.domain.RBook;
import com.flow.booktrade.dto.Book;
import com.flow.booktrade.dto.BookStatus;
import com.flow.booktrade.dto.User;
import com.flow.booktrade.dto.UserRole;
import com.flow.booktrade.exception.NoPermissionException;
import com.flow.booktrade.exception.ResourceNotFoundException;
import com.flow.booktrade.repository.BookJDBCRepository;
import com.flow.booktrade.repository.BookRepository;
import com.flow.booktrade.service.mapper.BookMapper;
import com.flow.booktrade.service.util.CompareUtil;
import com.flow.booktrade.service.util.RestPreconditions;

/**
 * Service to handle Book objects
 * @author Dayna
 *
 */
@Service
public class BookService extends BaseService {
	
	@Autowired
	private BookRepository bookRepo;
	
	@Autowired
	private BookJDBCRepository bookJDBCRepo;
	
	private BookMapper bookMapper = new BookMapper();

	/**
	 * Create a book to sell/trade
	 * @param owner
	 * @param book
	 * @return
	 */
	public Book createBook(User owner, Book book){
		RestPreconditions.checkNotNull(owner);
		RestPreconditions.checkNotNull(book);
		
		book.setOwner(owner);
		RBook rb = bookMapper.toRBook(book);
		rb.setStatus(BookStatus.AVAILABLE);	// set the book as available
		RBook saved = bookRepo.save(rb);
		return bookMapper.toBook(saved);
	}
	
	/**
	 * Update thes status of the book
	 * @param user
	 * @param book
	 * @return
	 * @throws NoPermissionException
	 */
	public Book updateBookAvailability(User user, Book book) throws NoPermissionException{
		RestPreconditions.checkNotNull(user);
		RestPreconditions.checkNotNull(book);
		RBook rb = bookRepo.findOne(book.getId());
		if(rb.getOwner().getId() != user.getId() && user.getRole().equals(UserRole.ADMIN)){
			throw new NoPermissionException("You must be the owner or an admin to update this book.");
		}
		
		if(!CompareUtil.compare(rb.getStatus(), book.getStatus())){
			rb.setStatus(book.getStatus());
			rb = bookRepo.save(rb);
		}
		
		return bookMapper.toBook(rb);
	}
	
	/**
	 * Delete a book if user is owner or admin
	 * @param owner
	 * @param bookId
	 * @return
	 * @throws NoPermissionException
	 */
	public Long removeBook(User owner, Long bookId) throws NoPermissionException{
		RestPreconditions.checkNotNull(owner);
		RestPreconditions.checkNotNull(bookId);
		RBook toDelete = bookRepo.findOne(bookId);
		if(!toDelete.getOwner().getId().equals(owner.getId()) && !owner.getRole().equals(UserRole.ADMIN)){
			throw new NoPermissionException("You must be an admin or the owner of this book to remove it.");
		}
		
		bookJDBCRepo.removeBookAndReferences(bookId);
		return bookId;
	}
	
	/**
	 * Update a book posting
	 * @param user
	 * @param book
	 * @return
	 * @throws Exception
	 */
	public Book updateBook(User user, Book book) throws Exception{
		RestPreconditions.checkNotNull(user);
		RestPreconditions.checkNotNull(book);
		RBook rb = loadBook(book.getId());
		if(rb.getOwner().getId() != user.getId() && !user.getRole().equals(UserRole.ADMIN)){
			throw new NoPermissionException("You must be the owner to update this book.");
		}
		
		boolean dirty = false;
		
		if(!CompareUtil.compare(rb.getCondition(), book.getCondition())){
			rb.setCondition(book.getCondition());
			if(!dirty){
				dirty = true;
			}
		}
		
		if(!CompareUtil.compare(rb.getDescription(), book.getDescription())){
			rb.setDescription(book.getDescription());
			if(!dirty){
				dirty = true;
			}
		}
		
		if(!CompareUtil.compare(rb.getStatus(), book.getStatus())){
			rb.setStatus(book.getStatus());
			if(!dirty){
				dirty = true;
			}
		}
		
		if(dirty){
			RBook updated = bookRepo.save(rb);
			return bookMapper.toBook(updated);
		}		
		
		return bookMapper.toBook(rb);
	}
	
	/**
	 * Find book by it's id
	 * @param bookId
	 * @return
	 * @throws ResourceNotFoundException 
	 */
	public Book findBookById(Long bookId) throws ResourceNotFoundException{
		RestPreconditions.checkNotNull(bookId);
		RBook rb = loadBook(bookId);
		return bookMapper.toBook(rb);
	}
	
	/**
	 * Search books by their author
	 * @param author
	 * @param pageable
	 * @return
	 */
	public Page<Book> searchBookByAuthorOrTitle(String value, Pageable pageable){
		RestPreconditions.checkNotNull(pageable);
		String lowerValue = value;
		if(value != null){
			lowerValue = value.toLowerCase();
		}
		
		String formatValue = "%" + lowerValue + "%";
		Page<RBook> results = bookRepo.searchBooksByAuthor(formatValue, pageable);
		return bookMapper.toBookPage(results, pageable);
	}
	
	/**
	 * Find most recent books
	 * @param user
	 * @param pageable
	 * @return
	 */
	public Page<Book> findMostRecentBooksInSameCity(User user, Pageable pageable){
		RestPreconditions.checkNotNull(user);
		RestPreconditions.checkNotNull(pageable);
		String city = user.getLocation() != null ? user.getLocation().getCity() : null;
		if(city != null){
			Page<RBook> nearbyBooks = bookRepo.findMostRecentBooksNearby(city, pageable);
			return bookMapper.toBookPage(nearbyBooks, pageable);
		}
		
		// If a user has not selected a city, just return the most recent books
		Page<RBook> allRecentBooks = bookRepo.findMostRecentBooks(pageable);
		return bookMapper.toBookPage(allRecentBooks, pageable);
	}
	
	/**
	 * Find available books by owner id
	 * @param user
	 * @param pageable
	 * @return
	 */
	public Page<Book> findBooksAvailableByOwnerId(Long userId, Pageable pageable){
		RestPreconditions.checkNotNull(userId);
		RestPreconditions.checkNotNull(pageable);
		Page<RBook> books = bookRepo.findBooksAvailableByOwnerId(userId, pageable);
		return bookMapper.toBookPage(books, pageable);
	}
	
	/**
	 * Find books that are unavailable by owner
	 * @param userId
	 * @param pageable
	 * @return
	 */
	public Page<Book> findUnavailableBooksByOwnerId(Long userId, Pageable pageable){
		RestPreconditions.checkNotNull(userId);
		RestPreconditions.checkNotNull(pageable);
		Page<RBook> books = bookRepo.findBooksUnavailableByOwnerId(userId, pageable);
		return bookMapper.toBookPage(books, pageable);
	}
	
	public Page<Book> filterBookSearch(Pageable pageable, Map<String, String> criteria, User currentUser){
		List<Book> filtered = bookJDBCRepo.filterBookSearch(criteria, currentUser);
		return new PageImpl<Book>(filtered, pageable, filtered.size());
	}

}
