package com.flow.booktrade.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

@Embeddable
public class UserConversationPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@OneToOne
	private RUser user;
	
	@OneToOne
	private RConversation conversation;
	
	public UserConversationPK(){}
	
	public UserConversationPK(RUser user, RConversation conversation){
		this.user = user;
		this.conversation = conversation;
	}

	public RUser getUser() {
		return user;
	}

	public void setUser(RUser user) {
		this.user = user;
	}

	public RConversation getConversation() {
		return conversation;
	}

	public void setConversation(RConversation conversation) {
		this.conversation = conversation;
	}
}
