package com.flow.booktrade.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.flow.booktrade.dto.User;
import com.flow.booktrade.exception.ResourceNotFoundException;
import com.flow.booktrade.service.UserService;

/**
 * Controller to handle User objects
 * @author Dayna
 *
 */
@RestController
@RequestMapping("/api")
public class UserController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * Update the current user's avatar
	 * @param avatar
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value = "/users/avatar", method = RequestMethod.PUT)
	@ResponseBody
	public User updateAvatar(@RequestBody final String avatar) throws ResourceNotFoundException{
		User user = getCurrentUser();
		User updated = userService.updateAvatar(user,  avatar);
		return updated;
	}
	
	
	
}