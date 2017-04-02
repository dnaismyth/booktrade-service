package com.flow.booktrade.domain;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name="user_conversation_activity", indexes={
		@Index(name="last_visited_idx", columnList="last_visited")
})
public class RUserConversationActivity {
	
	@Id
	private UserConversationPK pk;
	
	@Column(name="last_visited")
	private ZonedDateTime lastVisited;
	
	public RUserConversationActivity(){}
	
	public RUserConversationActivity(UserConversationPK pk, ZonedDateTime lastVisited){
		this.pk = pk;
		this.lastVisited = lastVisited;
	}

	public UserConversationPK getPk() {
		return pk;
	}

	public void setPk(UserConversationPK pk) {
		this.pk = pk;
	}

	public ZonedDateTime getLastVisited() {
		return lastVisited;
	}

	public void setLastVisited(ZonedDateTime lastVisited) {
		this.lastVisited = lastVisited;
	}
}
