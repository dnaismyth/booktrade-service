package com.flow.booktrade.service.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.flow.booktrade.domain.RNotification;
import com.flow.booktrade.dto.Comment;
import com.flow.booktrade.dto.Notification;

/**
 * Map helper for Notification objects
 * @author Dayna
 *
 */
public class NotificationMapper {

	private UserMapper userMapper = new UserMapper();
	private BookMapper bookMapper = new BookMapper();
	
	/**
	 * To Notification object for client side
	 * @param rn
	 * @return
	 */
	public Notification toNotification(RNotification rn){
		Notification n = null;
		if(rn != null){
			n = new Notification();
			n.setBook(bookMapper.toBook(rn.getBook()));
			n.setCreatedDate(rn.getCreatedDate());
			n.setId(rn.getId());
			n.setSender(userMapper.toUser(rn.getSender()));
			n.setType(rn.getType());
		}
		return n;
	}
	
	/**
	 * To Notification Page
	 * @param rn
	 * @param pageable
	 * @return
	 */
	public Page<Notification> toNotificationPage(Page<RNotification> rn, Pageable pageable){
		List<Notification> notifyList = new ArrayList<Notification>();
		Iterator<RNotification> iter = rn.iterator();
		while(iter.hasNext()){
			notifyList.add(toNotification(iter.next()));
		}
		
		return new PageImpl<Notification>(notifyList, pageable, notifyList.size());

	}
}
