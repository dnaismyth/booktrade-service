package com.flow.booktrade.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flow.booktrade.domain.RBook;
import com.flow.booktrade.domain.RComment;
import com.flow.booktrade.domain.RConversation;
import com.flow.booktrade.domain.RUser;
import com.flow.booktrade.dto.Comment;
import com.flow.booktrade.dto.User;
import com.flow.booktrade.dto.UserRole;
import com.flow.booktrade.exception.NoPermissionException;
import com.flow.booktrade.exception.ResourceNotFoundException;
import com.flow.booktrade.repository.BookRepository;
import com.flow.booktrade.repository.CommentRepository;
import com.flow.booktrade.repository.ConversationRepository;
import com.flow.booktrade.service.mapper.CommentMapper;
import com.flow.booktrade.service.util.RestPreconditions;


@Service
@Transactional
public class CommentService extends BaseService {
	
	@Autowired
	private BookRepository bookRepo;
	
	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private ConversationRepository conversationRepo;
	
	@Autowired
	private ConversationService conversationService;
	
	private CommentMapper commentMapper = new CommentMapper();
	
	/**
	 * Create a book comment
	 * @param comment
	 * @param commenter
	 * @return
	 * @throws ResourceNotFoundException 
	 */
	public Comment createBookComment(Comment comment, User commenter) throws ResourceNotFoundException{
		RestPreconditions.checkNotNull(comment);
		RestPreconditions.checkNotNull(commenter);
		comment.setCommenter(commenter);
		RComment rc = commentMapper.toRComment(comment);
		
		RBook rb = bookRepo.findOne(comment.getBookId());
		// TODO: Send notification to the book owner
		rc.setBook(rb);
		if(comment.getReplyTo() != null){
			RUser replyTo = loadUserEntity(comment.getReplyTo());
			rc.setReplyTo(replyTo);	// TODO: Send notification
		}
		
		RComment saved = commentRepo.save(rc);
		RConversation conversation = conversationRepo.findConversationByInitiatorAndBookId(commenter.getId(), rb.getId());
		if(conversation == null){
			RConversation convo = new RConversation();
			convo.getComments().add(saved);
			convo.setBook(rb);
			convo.setRecipient(rb.getOwner());
			conversationService.createConversation(saved.getCommenter(), convo);
		} else {
			conversation.getComments().add(saved);
			conversationRepo.save(conversation);
		}
		return commentMapper.toComment(saved);	
	}
	
	/**
	 * Find All comments by Book Id
	 * @param bookId
	 * @param pageable
	 * @return
	 */
	public Page<Comment> findAllCommentsByBookId(Long bookId, Pageable pageable){
		RestPreconditions.checkNotNull(bookId);
		RestPreconditions.checkNotNull(pageable);
		
		Page<RComment> results = commentRepo.findCommentsByBookId(bookId, pageable);
		Page<Comment> comments = commentMapper.toCommentPage(results, pageable);
		return comments;
	}
	
	/**
	 * Remove a comment
	 * @param currentUser
	 * @param commentId
	 * @return
	 * @throws ResourceNotFoundException 
	 */
	public Long removeCommentById(User currentUser, Long commentId) throws Exception {
		RestPreconditions.checkNotNull(commentId);
		RestPreconditions.checkNotNull(currentUser);
		
		RComment rc = commentRepo.findOne(commentId);
		if(rc == null){
			throw new ResourceNotFoundException("Cannot find comment with id=" + commentId);
		}
		
		if(rc.getCommenter().getId() != currentUser.getId() && !currentUser.getRole().equals(UserRole.ADMIN)){
			throw new NoPermissionException("You must be the owner or an admin to remove this comment");
		}
		
		commentRepo.delete(rc);
		return commentId;	
	}
}
