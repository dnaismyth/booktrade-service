package com.flow.booktrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flow.booktrade.domain.RConversation;
import com.flow.booktrade.domain.RUser;
import com.flow.booktrade.dto.Conversation;
import com.flow.booktrade.dto.User;
import com.flow.booktrade.exception.NoPermissionException;
import com.flow.booktrade.repository.ConversationRepository;
import com.flow.booktrade.service.mapper.ConversationMapper;
import com.flow.booktrade.service.util.RestPreconditions;

@Service
public class ConversationService {
	
	@Autowired
	private ConversationRepository conversationRepo;
	
	private ConversationMapper convoMapper = new ConversationMapper();
	
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
	
	/**
	 * Find One conversation by it's id
	 * @param convoId
	 * @return
	 * @throws NoPermissionException 
	 */
	public Conversation findConversationById(User user, Long convoId) throws NoPermissionException{
		RestPreconditions.checkNotNull(convoId);
		RConversation rc = conversationRepo.findOne(convoId);
		if(!rc.getRecipient().getId().equals(user.getId()) || !rc.getInitiator().equals(user.getId())){
			throw new NoPermissionException("You do not have permission to view this conversation.");
		}
		return convoMapper.toConversation(rc, true);
	}
	
	/**
	 * Find all conversations by recipient id
	 * @param recipientId
	 * @param pageable
	 * @return
	 */
	public Page<Conversation> getConversationsByRecipientId(Long recipientId, Pageable pageable){
		RestPreconditions.checkNotNull(pageable);
		RestPreconditions.checkNotNull(recipientId);
		Page<RConversation> results = conversationRepo.findConversationsByRecipientId(recipientId, pageable);
		return convoMapper.toConversationPage(results, pageable, false);
	}
	
	public Page<Conversation> getConversationsByInitiatorId(Long initiatorId, Pageable pageable){
		RestPreconditions.checkNotNull(pageable);
		RestPreconditions.checkNotNull(initiatorId);
		Page<RConversation> results = conversationRepo.findConversationsByInitiatorId(initiatorId, pageable);
		return convoMapper.toConversationPage(results, pageable, false);
	}
	
}
