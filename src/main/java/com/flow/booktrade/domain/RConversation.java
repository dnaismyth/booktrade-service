package com.flow.booktrade.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;


@Entity
@Table(name="conversation")
public class RConversation extends AbstractAuditingEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToOne
	private RUser initiator;	// user that initiated the conversation
	
	@OneToOne
	private RUser recipient;	// user that received the initial message
	
	@OneToOne
	private RBook book;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name="conversation_comment",
			joinColumns = {@JoinColumn(name = "conversation_id")},
			inverseJoinColumns = {@JoinColumn(name = "comment_id")}
	)
	private List<RComment> comments = new ArrayList<RComment>();
	
	public RConversation(){}
	
	public RConversation(RUser initiator){
		this.initiator = initiator;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<RComment> getComments() {
		return comments;
	}

	public void setComments(List<RComment> comments) {
		this.comments = comments;
	}

	public RUser getInitiator() {
		return initiator;
	}

	public void setInitiator(RUser initiator) {
		this.initiator = initiator;
	}
	
	public RBook getBook(){
		return book;
	}
	
	public void setBook(RBook book){
		this.book = book;
	}

	public RUser getRecipient() {
		return recipient;
	}

	public void setRecipient(RUser recipient) {
		this.recipient = recipient;
	}
	
}
