package com.flow.booktrade.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.BasicSessionCredentials;
import com.flow.booktrade.dto.Location;
import com.flow.booktrade.dto.OperationType;
import com.flow.booktrade.dto.User;
import com.flow.booktrade.exception.NoPermissionException;
import com.flow.booktrade.exception.ResourceNotFoundException;
import com.flow.booktrade.service.S3TokenService;
import com.flow.booktrade.service.UserService;
import com.flow.booktrade.service.util.S3Utils;
import com.flow.booktrade.web.rest.vm.RestResponse;
import com.flow.booktrade.web.rest.vm.S3TokenResponse;
import com.flow.booktrade.web.rest.vm.SimpleRequest;

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
	
	@Autowired
	private S3TokenService s3TokenService;
	
	/**
	 * Update the current user's avatar
	 * @param avatar
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value = "/users/avatar", method = RequestMethod.PUT)
	@ResponseBody
	public User updateAvatar(@RequestBody final SimpleRequest request) throws ResourceNotFoundException{
		User user = getCurrentUser();
		User updated = userService.updateAvatar(user,  request.getValue());
		return updated;
	}
	
	/**
	 * Update the current user's device token
	 * @param request
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value = "/users/devicetoken", method = RequestMethod.PUT)
	@ResponseBody
	public RestResponse<User> updateDeviceToken(@RequestBody final SimpleRequest request) throws ResourceNotFoundException{
		User user = getCurrentUser();
		User updated = userService.updateDeviceToken(user, request.getValue());
		return new RestResponse<User>(updated, OperationType.UPDATE);
	}
	
	/**
	 * Allow user access to S3 Bucket
	 * @return 
	 * @throws NoPermissionException 
	 */
	@RequestMapping(value = "/users/s3token", method = RequestMethod.GET)
	@ResponseBody
	public S3TokenResponse getS3AccessToken() throws NoPermissionException{
		User user = getCurrentUser();
		BasicSessionCredentials credentials = s3TokenService.getS3UserCredentials();
		return new S3TokenResponse(credentials, S3Utils.S3_BUCKET);
	}
	
	/**
	 * Get a user's profile by their id
	 * @param userId
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value = "/users/profile", method = RequestMethod.GET)
	@ResponseBody
	public User getUserProfile(@RequestParam(value="id", required = false) Long id) throws ResourceNotFoundException{
		if(id == null){
			User user = getCurrentUser();	// return the current user if id is null
			return user;
		} else {
			User found = userService.getUserById(id);	// otherwise, find user by their id and return
			return found;
		}	
	}
	
	/**
	 * Update the current user's location
	 * @param location
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value = "/users/location", method = RequestMethod.PUT)
	@ResponseBody
	public User updateUserLocation(@RequestBody final Location location) throws ResourceNotFoundException{
		User user = getCurrentUser();
		User updated = userService.updateLocation(user, location);
		return updated;
	}
	
	
}