package com.flow.booktrade.service.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.flow.booktrade.domain.RComment;
import com.flow.booktrade.dto.Comment;


/**
 * Helper class to map comments
 * @author Dayna
 *
 */
public class CommentMapper {
	
	private BookMapper bookMapper = new BookMapper();
	private UserMapper userMapper = new UserMapper();

	public RComment toRComment(Comment c){
		RComment rc = null;
		if(c != null){
			rc = new RComment();
			rc.setCommenter(userMapper.toRUser(c.getCommenter()));
			rc.setText(c.getText());
			rc.setId(c.getId());
		}
		return rc;
	}
	
	public Comment toComment(RComment rc){
		Comment c = null;
		if(rc != null){
			c = new Comment();
			c.setBookId(rc.getBook().getId());
			c.setCommenter(userMapper.toUser(rc.getCommenter()));
			c.setCreatedDate(rc.getCreatedDate());
			c.setId(rc.getId());
			c.setText(rc.getText());
			Long replyToId = rc.getReplyTo() != null ? rc.getReplyTo().getId() : null;
			c.setReplyTo(replyToId);
		}
		
		return c;
	}
	
	/**
	 * Return a page of Comments
	 * @param rc
	 * @param pageable
	 * @return
	 */
	public Page<Comment> toCommentPage(Page<RComment> rc, Pageable pageable){
		List<Comment> comments = new ArrayList<Comment>();
		Iterator<RComment> iter = rc.iterator();
		while(iter.hasNext()){
			comments.add(toComment(iter.next()));
		}
		return new PageImpl<Comment>(comments, pageable, comments.size());
	}	
}
