package com.flow.booktrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flow.booktrade.domain.RBook;
import com.flow.booktrade.domain.RNotification;
import com.flow.booktrade.dto.Book;
import com.flow.booktrade.dto.Notification;
import com.flow.booktrade.dto.NotificationType;
import com.flow.booktrade.dto.User;
import com.flow.booktrade.repository.BookRepository;
import com.flow.booktrade.repository.NotificationRepository;
import com.flow.booktrade.service.mapper.NotificationMapper;
import com.flow.booktrade.service.mapper.UserMapper;
import com.flow.booktrade.service.util.RestPreconditions;

@Service
public class NotificationService extends BaseService {
	
	@Autowired
	private NotificationRepository notifyRepo;
	
	@Autowired
	private BookRepository bookRepo;

	private UserMapper userMapper = new UserMapper(); 
	private NotificationMapper notifyMapper = new NotificationMapper();
	
	public void sendBookCommentNotification(Book book, User sender){
		RestPreconditions.checkNotNull(book);
		RestPreconditions.checkNotNull(sender);
		saveNotification(book, sender, NotificationType.COMMENT);
		// TODO: send push notification
	}
	
	/**
	 * Find all notifications by receiver id
	 * @param currentUser
	 * @param pageable
	 * @return
	 */
	public Page<Notification> findNotificationsByReceiverId(User currentUser, Pageable pageable){
		RestPreconditions.checkNotNull(currentUser);
		Page<RNotification> rn = notifyRepo.findNotificationsByReceiverId(currentUser.getId(), pageable);
		Page<Notification> notifications = notifyMapper.toNotificationPage(rn, pageable);
		return notifications;
	}
	
	/**
	 * Save notifications
	 * @param book
	 * @param sender
	 */
	private void saveNotification(Book book, User sender, NotificationType type){
		RBook rb = bookRepo.findOne(book.getId());
		RNotification rn = new RNotification();
		rn.setBook(rb);
		rn.setReceiver(rb.getOwner());
		rn.setSender(userMapper.toRUser(sender));
		rn.setType(type);
		notifyRepo.save(rn);
	}
}
