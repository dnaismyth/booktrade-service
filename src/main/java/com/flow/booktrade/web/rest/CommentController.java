package com.flow.booktrade.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.flow.booktrade.dto.Comment;
import com.flow.booktrade.dto.OperationType;
import com.flow.booktrade.dto.User;
import com.flow.booktrade.exception.ResourceNotFoundException;
import com.flow.booktrade.service.CommentService;
import com.flow.booktrade.web.rest.vm.RestResponse;

/**
 * Controller for Comments
 * @author Dayna
 *
 */
@RestController
@RequestMapping(value = "/api")
public class CommentController extends BaseController {
	
	@Autowired
	private CommentService commentService;

	/**
	 * Allow for the owner or an admin to delete a comment
	 * @param commentId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/comments/{commentId}", method = RequestMethod.DELETE)
	@ResponseBody
	public RestResponse<Long> removeComment(@PathVariable("commentId") Long commentId) throws Exception{
		User user = getCurrentUser();
		Long deletedId = commentService.removeCommentById(user, commentId);
		return new RestResponse<Long>(deletedId, OperationType.DELETE);
	}
	
	/**
	 * Find all comments by book id
	 * @param bookId
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/books/{bookId}/comments", method = RequestMethod.GET)
	@ResponseBody
	public Page<Comment> getCommentsByBookId(@PathVariable("bookId") Long bookId, @RequestParam(value = PARAM_PAGE, required = true) int page, 
			@RequestParam(value = PARAM_SIZE, required = true) int size){
		Page<Comment> results = commentService.findAllCommentsByBookId(bookId, new PageRequest(page, size));
		return results;
	}
	
	/**
	 * Create a book comment
	 * @param bookId
	 * @param comment
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value="/books/{bookId}/comments", method = RequestMethod.POST)
	@ResponseBody
	public Comment createBookComment(@PathVariable("bookId") Long bookId, @RequestBody final Comment comment) throws ResourceNotFoundException{
		User user = getCurrentUser();
		comment.setBookId(bookId);
		Comment created = commentService.initiateConversationWithBookComment(comment, user);
		return created;
	}
	
	/**
	 * Allow for a user to post a new comment to an existing conversation
	 * @param convoId
	 * @param comment
	 * @return
	 */
	@RequestMapping(value="/conversations/{convoId}/comments", method = RequestMethod.POST)
	@ResponseBody
	public Comment postCommentToConversation(@PathVariable("convoId") Long convoId, @RequestBody final Comment comment){
		User user = getCurrentUser();
		Comment created = commentService.postCommentToConversation(convoId, comment, user);
		return created;
	}
	
}
