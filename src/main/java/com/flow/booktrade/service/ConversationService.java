package com.flow.booktrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flow.booktrade.domain.RConversation;
import com.flow.booktrade.domain.RUser;
import com.flow.booktrade.repository.ConversationRepository;
import com.flow.booktrade.service.util.RestPreconditions;

@Service
public class ConversationService {
	
	@Autowired
	private ConversationRepository conversationRepo;
	
	/**
	 * Create a new conversation.
	 * Expose RConversation to avoid redundant mapping.
	 * @param initiator
	 * @param conversation
	 * @return
	 */
	public RConversation createConversation(RUser initiator, RConversation conversation){
		RestPreconditions.checkNotNull(initiator);
		RestPreconditions.checkNotNull(conversation);
		conversation.setInitiator(initiator);
		return conversationRepo.save(conversation);
	}
}
