package com.flow.booktrade.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.flow.booktrade.dto.NotificationType;

@Entity
@Table(name="notification")
public class RNotification extends AbstractAuditingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private RUser sender;
	
	@ManyToOne
	private RUser receiver;
	
	@ManyToOne
	private RBook book;
	
    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private NotificationType type;

	public RNotification(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RUser getSender() {
		return sender;
	}

	public void setSender(RUser sender) {
		this.sender = sender;
	}

	public RUser getReceiver() {
		return receiver;
	}

	public void setReceiver(RUser receiver) {
		this.receiver = receiver;
	}

	public RBook getBook() {
		return book;
	}

	public void setBook(RBook book) {
		this.book = book;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}
	
}
