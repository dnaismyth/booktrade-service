package com.flow.booktrade.service;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flow.booktrade.domain.RConversation;
import com.flow.booktrade.domain.RUser;
import com.flow.booktrade.domain.RUserConversationActivity;
import com.flow.booktrade.domain.UserConversationPK;
import com.flow.booktrade.dto.Conversation;
import com.flow.booktrade.dto.User;
import com.flow.booktrade.exception.NoPermissionException;
import com.flow.booktrade.exception.ResourceNotFoundException;
import com.flow.booktrade.repository.ConversationRepository;
import com.flow.booktrade.repository.UserConversationActivityJDBCRepository;
import com.flow.booktrade.repository.UserConversationActivityRepository;
import com.flow.booktrade.service.mapper.ConversationMapper;
import com.flow.booktrade.service.util.RestPreconditions;

@Service
@Transactional
public class ConversationService extends BaseService {
	
	@Autowired
	private ConversationRepository conversationRepo;
	
	@Autowired
	private UserConversationActivityJDBCRepository convoActivityJDBCRepo;
	
	@Autowired
	private UserConversationActivityRepository userActivityRepo;
	
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
	 * @throws ResourceNotFoundException 
	 */
	public Conversation findConversationById(User user, Long convoId) throws NoPermissionException, ResourceNotFoundException{
		RestPreconditions.checkNotNull(convoId);
		RestPreconditions.checkNotNull(user);
		RConversation rc = conversationRepo.findOne(convoId);
		RUser ru = loadUserEntity(user.getId());
		saveConversationActivity(ru, rc);	// save the conversation activity so we know when the user has last checked their messages
		if(!(rc.getRecipient().getId() != user.getId()) && !(rc.getInitiator().getId() != user.getId())){
			throw new NoPermissionException("You do not have permission to view this conversation.");
		}
	
		return convoMapper.toConversation(rc, true, false);
	}
	
	/**
	 * Save the last visted time/date of the conversation
	 * @param ru (user entity)
	 * @param rc (conversation entity)
	 */
	private void saveConversationActivity(RUser ru, RConversation rc){
		UserConversationPK pk = new UserConversationPK(ru, rc);
		RUserConversationActivity activity = userActivityRepo.findOne(pk);
		if(activity != null){
			activity.setLastVisited(ZonedDateTime.now());	// update their last visited time to now
			userActivityRepo.save(activity);
		} else {
			activity = new RUserConversationActivity(pk, ZonedDateTime.now());
			userActivityRepo.save(activity);	 // create new activity if it does not exist
		}
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
		Page<Conversation> convos = convoMapper.toConversationPage(results, pageable, false, false);
		Map<Long, Conversation> convoMap = buildConversationIdMap(convos.getContent());
		Map<Long, Integer> messageCount = convoActivityJDBCRepo.getUnreadMessageCount(convoMap.keySet(), recipientId);
		setUnreadMessageCount(convos.getContent(), messageCount);
		return convos;
	}
	
	/**
	 * Find all conversations by initiator id
	 * @param initiatorId
	 * @param pageable
	 * @return
	 */
	public Page<Conversation> getConversationsByInitiatorId(Long initiatorId, Pageable pageable){
		RestPreconditions.checkNotNull(pageable);
		RestPreconditions.checkNotNull(initiatorId);
		Page<RConversation> results = conversationRepo.findConversationsByInitiatorId(initiatorId, pageable);
		Page<Conversation> convos = convoMapper.toConversationPage(results, pageable, false, false);
		Map<Long, Conversation> convoMap = buildConversationIdMap(convos.getContent());
		Map<Long, Integer> messageCount = convoActivityJDBCRepo.getUnreadMessageCount(convoMap.keySet(), initiatorId);
		setUnreadMessageCount(convos.getContent(), messageCount);
		return convos;
	}
	
	private void setUnreadMessageCount(List<Conversation> convos, Map<Long, Integer> messageCount){
		for(Conversation c : convos){
			if(messageCount.containsKey(c.getId())){
				c.setUnreadMessageCount(messageCount.get(c.getId()));
			}
		}
	}
	
	private Map<Long, Conversation> buildConversationIdMap(Collection<Conversation> convos){
		Map<Long, Conversation> map = new HashMap<Long, Conversation>();
		for(Conversation c : convos){
			map.put(c.getId(), c);
		}
		return map;
	}
	
}
