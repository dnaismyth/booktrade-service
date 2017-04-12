package com.flow.booktrade.web.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.flow.booktrade.dto.User;
import com.flow.booktrade.service.UserService;
import com.flow.booktrade.web.rest.vm.ManagedUserVM;

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
	
	protected boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH);
    }
}
