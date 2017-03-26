package com.flow.booktrade.service.mapper;

import com.flow.booktrade.domain.RNotification;
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
}
