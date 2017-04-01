package com.flow.booktrade.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.flow.booktrade.dto.Conversation;
import com.flow.booktrade.dto.User;
import com.flow.booktrade.exception.NoPermissionException;
import com.flow.booktrade.service.ConversationService;
import com.flow.booktrade.web.rest.BaseController;

@RestController
@RequestMapping("/api")
public class ConversationController extends BaseController {
	
	@Autowired
	private ConversationService convoService;
	
	/**
	 * Find all conversations where the current user has the role as the recipient.
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/conversations/recipient", method = RequestMethod.GET)
	@ResponseBody
	public Page<Conversation> getConversationsWhereCurrentUserIsRecipient(@RequestParam(PARAM_PAGE) int page, @RequestParam(PARAM_SIZE) int size){
		User user = getCurrentUser();
		return convoService.getConversationsByRecipientId(user.getId(), new PageRequest(page, size));
	}
	
	/**
	 * Find all conversations where the current user has the role as the initiator.
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/conversations/initiator", method = RequestMethod.GET)
	@ResponseBody
	public Page<Conversation> getConversationsWhereCurrentUserIsInitiator(@RequestParam(PARAM_PAGE) int page, @RequestParam(PARAM_SIZE) int size){
		User user = getCurrentUser();
		return convoService.getConversationsByInitiatorId(user.getId(), new PageRequest(page, size));
	}
	
	/**
	 * Find conversation by it's id.
	 * Only the recipient / initiator OR admin will have permission to view this conversation.
	 * @param convoId
	 * @return
	 * @throws NoPermissionException
	 */
	@RequestMapping(value = "conversations/{convoId}", method = RequestMethod.GET)
	@ResponseBody
	public Conversation getConversationById(@PathVariable("convoId") Long convoId) throws NoPermissionException{
		User user = getCurrentUser();
		return convoService.findConversationById(user, convoId);
	}
}
