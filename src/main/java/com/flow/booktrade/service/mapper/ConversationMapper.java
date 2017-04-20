package com.flow.booktrade.service.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.flow.booktrade.domain.RComment;
import com.flow.booktrade.domain.RConversation;
import com.flow.booktrade.dto.Conversation;

public class ConversationMapper {
	
	private BookMapper bookMapper = new BookMapper();
	private UserMapper userMapper = new UserMapper();
	private CommentMapper commentMapper = new CommentMapper();

	/**
	 * To Conversation
	 * @param rc
	 * @param showComments
	 * @return
	 */
	public Conversation toConversation(RConversation rc, boolean showComments, boolean showBookCategories){
		Conversation c = null;
		if(rc != null){
			c = new Conversation();
			c.setBook(bookMapper.toBook(rc.getBook(), showBookCategories));
			c.setCreatedDate(rc.getCreatedDate());
			c.setInitiator(userMapper.toUser(rc.getInitiator()));
			c.setRecipient(userMapper.toUser(rc.getRecipient()));
			c.setId(rc.getId());
			if(showComments){
				for(RComment comment : rc.getComments()){
					c.getComments().add(commentMapper.toComment(comment));
				}
			}
		}
		
		return c;
	}
	
	/**
	 * To Conversation Page
	 * @param rc
	 * @param pageable
	 * @param showComments
	 * @return
	 */
	public Page<Conversation> toConversationPage(Page<RConversation> rc, Pageable pageable, boolean showComments, boolean showBookCategories){
		List<Conversation> convo = new ArrayList<Conversation>();
		Iterator<RConversation> iter = rc.iterator();
		while(iter.hasNext()){
			convo.add(toConversation(iter.next(), showComments, showBookCategories));
		}
		
		return new PageImpl<Conversation>(convo, pageable, convo.size());
	}
	
}
