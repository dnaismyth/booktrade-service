package com.flow.booktrade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flow.booktrade.domain.RConversation;

public interface ConversationRepository extends JpaRepository<RConversation, Long> {
	
	@Query("SELECT rc FROM RConversation rc WHERE rc.initiator.id =?1 AND rc.book.id =?2")
	public RConversation findConversationByInitiatorAndBookId(Long initiatorId, Long bookId);
	
	
}
