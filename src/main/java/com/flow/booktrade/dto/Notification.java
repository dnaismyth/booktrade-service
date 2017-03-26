package com.flow.booktrade.dto;

import java.time.ZonedDateTime;

/**
 * DTO For Notification objects
 * @author Dayna
 *
 */
public class Notification {
	
	private Long id;
	private User sender;
	private NotificationType type;
	private Book book;
	private ZonedDateTime createdDate;
	
	public Notification(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}

}
