package com.flow.booktrade.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Comment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long replyToId;	// id of the user in which the comment could potentially reply to
	private User commenter;
	private ZonedDateTime createdDate;
	private String text;
	private Long bookId;
	
	public Comment(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReplyTo() {
		return replyToId;
	}

	public void setReplyTo(Long replyToId) {
		this.replyToId = replyToId;
	}

	public User getCommenter() {
		return commenter;
	}

	public void setCommenter(User commenter) {
		this.commenter = commenter;
	}

	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
}
