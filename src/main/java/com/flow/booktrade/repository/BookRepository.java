package com.flow.booktrade.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.flow.booktrade.domain.RBook;

public interface BookRepository extends JpaRepository<RBook, Long>, JpaSpecificationExecutor {
	
	@Query("SELECT rb FROM RBook rb WHERE lower(rb.title) LIKE (?1) ORDER BY rb.createdDate DESC")
	public Page<RBook> searchBooksByTitle(String title, Pageable pageable);
	
	@Query("SELECT rb FROM RBook rb WHERE lower(rb.author) LIKE (?1) ORDER BY rb.createdDate DESC")
	public Page<RBook> searchBooksByAuthor(String author, Pageable pageable);
	
	@Query("SELECT rb FROM RBook rb WHERE lower(rb.owner.location.city) = lower(?1) ORDER BY rb.createdDate DESC")
	public Page<RBook> findMostRecentBooksNearby(String city, Pageable pageable);
	
	@Query("SELECT rb FROM RBook rb ORDER BY rb.createdDate DESC")
	public Page<RBook> findMostRecentBooks(Pageable pageable);
	
	@Query("SELECT rb FROM RBook rb WHERE rb.owner.id =?1 AND rb.status = 'AVAILABLE' ORDER BY rb.createdDate DESC")
	public Page<RBook> findBooksAvailableByOwnerId(Long id, Pageable pageable);
	
	@Query("SELECT rb FROM RBook rb WHERE rb.owner.id =?1 AND rb.status != 'AVAILABLE' ORDER BY rb.createdDate DESC")
	public Page<RBook> findBooksUnavailableByOwnerId(Long id, Pageable pageable);
}
