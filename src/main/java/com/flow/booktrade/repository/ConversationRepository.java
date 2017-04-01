package com.flow.booktrade.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flow.booktrade.domain.RConversation;

public interface ConversationRepository extends JpaRepository<RConversation, Long> {
	
	@Query("SELECT rc FROM RConversation rc WHERE rc.initiator.id =?1 AND rc.book.id =?2")
	public RConversation findConversationByInitiatorAndBookId(Long initiatorId, Long bookId);
	
	@Query("SELECT rc FROM RConversation rc WHERE rc.recipient.id =?1 ORDER BY rc.createdDate DESC")
	public Page<RConversation> findConversationsByRecipientId(Long recipientId, Pageable pageable);
	
	@Query("SELECT rc FROM RConversation rc WHERE rc.initiator.id =?1 ORDER BY rc.createdDate DESC")
	public Page<RConversation> findConversationsByInitiatorId(Long initiatorId, Pageable pageable);
}
