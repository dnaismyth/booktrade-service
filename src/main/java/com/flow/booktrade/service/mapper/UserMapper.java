package com.flow.booktrade.service.mapper;

import com.flow.booktrade.domain.RUser;
import com.flow.booktrade.dto.User;

/**
 * Mapper for the entity User and Dto
 */
public class UserMapper {
	
	private LocationMapper locationMapper = new LocationMapper();

	/**
	 * To entity user
	 * @param u
	 * @return
	 */
    public RUser toRUser(User u){
    	RUser ru = null;
    	
    	if( u != null) {
    		ru = new RUser();
    		ru.setId(u.getId());
    		ru.setPassword(u.getPassword());
    		ru.setLogin(u.getLogin());
    		ru.setEmail(u.getEmail());
    		ru.setUserRole(u.getRole());
    		ru.setName(u.getName());
    		ru.setActivated(u.isActivated());
    		ru.setLocation(locationMapper.toRLocation(u.getLocation()));
    		ru.setLangKey(u.getLangKey());
    		ru.setAvatar(u.getAvatar());
    		ru.setBio(u.getBio());
    		ru.setPushNotification(u.getPushNotification());
    	}
  
    	return ru;
    }
    
    /**
     * To User DTO
     * @param ru
     * @return
     */
    public User toUser(RUser ru){
    	User u = null;
    	
    	if(ru != null){
    		u = new User();
    		u.setId(ru.getId());
    		u.setActivated(ru.getActivated());
    		u.setEmail(ru.getEmail());
    		u.setLogin(ru.getLogin());
    		u.setPassword(ru.getPassword());
    		u.setName(ru.getName());
    		u.setRole(ru.getUserRole());	
    		u.setLocation(locationMapper.toLocation(ru.getLocation()));
    		u.setLangKey(ru.getLangKey());
    		u.setAvatar(ru.getAvatar());
    		u.setBio(ru.getBio());
    		u.setPushNotification(ru.getPushNotification());
    	}
    	
    	return u;
    }
}
