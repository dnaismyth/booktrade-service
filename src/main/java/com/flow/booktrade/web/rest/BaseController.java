package com.flow.booktrade.web.rest;

import org.springframework.beans.factory.annotation.Autowired;

import com.flow.booktrade.dto.User;
import com.flow.booktrade.service.BookService;
import com.flow.booktrade.service.UserService;

public class BaseController {
	
	protected static final String PARAM_PAGE = "page";
	protected static final String PARAM_SIZE = "size";

	@Autowired
	private UserService userService;

	
	/**
	 * Return the current logged in user
	 * @return
	 */
	protected User getCurrentUser(){
		User current = userService.getCurrentUserDTOWithAuthorities();
		return current;
	}
}
