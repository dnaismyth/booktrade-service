package com.flow.booktrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.flow.booktrade.domain.RBook;
import com.flow.booktrade.domain.RNotification;
import com.flow.booktrade.domain.RUser;
import com.flow.booktrade.dto.Book;
import com.flow.booktrade.dto.Comment;
import com.flow.booktrade.dto.Notification;
import com.flow.booktrade.dto.NotificationType;
import com.flow.booktrade.dto.User;
import com.flow.booktrade.repository.BookRepository;
import com.flow.booktrade.repository.NotificationRepository;
import com.flow.booktrade.repository.UserRepository;
import com.flow.booktrade.service.mapper.NotificationMapper;
import com.flow.booktrade.service.mapper.UserMapper;
import com.flow.booktrade.service.util.RestPreconditions;
import com.flow.booktrade.service.util.firebase.FirebaseMobilePush;
import com.hazelcast.core.HazelcastInstance;

@Service
public class NotificationService extends BaseService {
	
	@Autowired
	private NotificationRepository notifyRepo;
	
	@Autowired
	private BookRepository bookRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private HazelcastInstance hazelcastInstance;
	
	private static final String NOTIFY_COMMENT_TITLE = "New message from ";

	private UserMapper userMapper = new UserMapper(); 
	private NotificationMapper notifyMapper = new NotificationMapper();
	
	public void sendBookCommentNotification(Book book, User sender, Comment comment){
		RestPreconditions.checkNotNull(book);
		RestPreconditions.checkNotNull(sender);
		RestPreconditions.checkNotNull(comment);
		saveNotification(book, sender, NotificationType.COMMENT);
		User bookOwner = book.getOwner();
		sendCommentNotification(bookOwner, sender, comment, book);
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
		Page<Notification> notifications = notifyMapper.toNotificationPage(rn, pageable, false);
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
	
	@Async
	private void sendCommentNotification(User receiver, User sender, Comment comment, Book book){
		RUser ru = userRepo.findOne(receiver.getId());
		if(ru.getPushNotification() && ru.getDeviceToken() != null){
			String messageTitle = NOTIFY_COMMENT_TITLE.concat(sender.getName() + ".");
			FirebaseMobilePush.sendNotification(ru.getDeviceToken(), messageTitle, comment.getText());
		}
	}
}
