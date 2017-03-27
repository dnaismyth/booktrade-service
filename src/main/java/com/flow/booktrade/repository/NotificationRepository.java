package com.flow.booktrade.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flow.booktrade.domain.RNotification;

public interface NotificationRepository extends JpaRepository<RNotification, Long>{
	
	@Query("SELECT rn FROM RNotification rn WHERE rn.receiver.id = ?1 ORDER BY rn.createdDate DESC")
	public Page<RNotification> findNotificationsByReceiverId(Long receiverId, Pageable pageable);
}
