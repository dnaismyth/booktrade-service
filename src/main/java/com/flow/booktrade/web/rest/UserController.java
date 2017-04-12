package com.flow.booktrade.web.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
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
import com.flow.booktrade.service.TokenService;
import com.flow.booktrade.service.UserService;
import com.flow.booktrade.service.util.S3Utils;
import com.flow.booktrade.web.rest.vm.ChangePasswordRequest;
import com.flow.booktrade.web.rest.vm.RestResponse;
import com.flow.booktrade.web.rest.vm.S3TokenResponse;
import com.flow.booktrade.web.rest.vm.SimpleRequest;
import com.flow.booktrade.web.rest.vm.UpdatePasswordResponse;


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
	
	@Autowired
	private TokenService tokenService;
	
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
	
	/**
	 * Update the current user's name
	 * @param request
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value="/users/name", method = RequestMethod.PUT)
	@ResponseBody
	public User updateUserName(@RequestBody final SimpleRequest request) throws ResourceNotFoundException{
		User user = getCurrentUser();
		User updated = userService.updateName(user, request.getValue());
		return updated;
	}
	
	/**
	 * Update the current user's bio
	 * @param request
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value="/users/bio", method = RequestMethod.PUT)
	@ResponseBody
	public User updateBio(@RequestBody final SimpleRequest request) throws ResourceNotFoundException{
		User user = getCurrentUser();
		User updated = userService.updateBio(user, request.getValue());
		return updated;
	}
	
	/**
     * Change user password
     * @param password
     * @return
     * @throws Exception 
     */
    @RequestMapping(path = "/users/password", method = RequestMethod.PUT)
    @ResponseBody
    public UpdatePasswordResponse changePassword(@RequestBody final ChangePasswordRequest passwordRequest, HttpServletRequest req) throws Exception {
    	User user = getCurrentUser();
    	String token = req.getHeader("Authorization");
        if (!checkPasswordLength(passwordRequest.getNewPassword())) {
            return new UpdatePasswordResponse(OperationType.NO_CHANGE);
        }
        boolean updated = userService.changePassword(passwordRequest);
        // If password has successfully been updated, revoke token and issue a new one
        if(updated){
        		OAuth2AccessToken newToken = tokenService.grantNewUserAccessToken(user);
        		if(newToken != null){
                	tokenService.revokeToken(token);	// revoke prior token
            		return new UpdatePasswordResponse(OperationType.UPDATE, newToken);	// return new generated token
        		}
        }
        return new UpdatePasswordResponse(OperationType.NO_CHANGE);
    }
	
	/**
	 * Update user's push notification setting
	 * @param pushNotification
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value = "/users/settings/notification", method = RequestMethod.PUT)
	@ResponseBody
	public User updatePushNotificationSettings(@RequestBody final SimpleRequest request) throws ResourceNotFoundException{
		User user = getCurrentUser();
		Boolean pushNotification = false;
		if(request.getValue().equals("true")){
			pushNotification = true;
		}
		User updated = userService.updatePushNotificationSetting(user, pushNotification);
		return updated;
	}
	
	
}