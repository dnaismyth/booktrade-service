package com.flow.booktrade.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Table to store comments
 * @author Dayna
 *
 */
@Entity
@Table(name="comment")
public class RComment extends AbstractAuditingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private RBook book;
	
	@ManyToOne
	private RUser commenter;
	
	@ManyToOne
	private RUser replyTo;
	
	@Column(name="text", length = 1024)
	private String text;
	
	public RComment(){}
	
	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}

	public RBook getBook() {
		return book;
	}

	public void setBook(RBook book) {
		this.book = book;
	}

	public RUser getCommenter() {
		return commenter;
	}

	public void setCommenter(RUser commenter) {
		this.commenter = commenter;
	}

	public RUser getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(RUser replyTo) {
		this.replyTo = replyTo;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
