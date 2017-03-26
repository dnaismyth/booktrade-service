package com.flow.booktrade.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flow.booktrade.domain.RComment;

public interface CommentRepository extends JpaRepository<RComment, Long> {

	@Query("SELECT rc FROM RComment rc WHERE rc.book.id = ?1 ORDER BY rc.createdDate DESC")
	public Page<RComment> findCommentsByBookId(Long bookId, Pageable pageable);
}
