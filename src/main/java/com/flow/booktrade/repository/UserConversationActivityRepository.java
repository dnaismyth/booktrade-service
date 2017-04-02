package com.flow.booktrade.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flow.booktrade.domain.RUserConversationActivity;
import com.flow.booktrade.domain.UserConversationPK;

public interface UserConversationActivityRepository extends JpaRepository<RUserConversationActivity, UserConversationPK> {

}
