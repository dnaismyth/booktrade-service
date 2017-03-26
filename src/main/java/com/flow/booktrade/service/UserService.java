package com.flow.booktrade.service;

import com.flow.booktrade.domain.RUser;
import com.flow.booktrade.dto.Platform;
import com.flow.booktrade.dto.User;
import com.flow.booktrade.dto.UserRole;
import com.flow.booktrade.exception.BadRequestException;
import com.flow.booktrade.exception.ResourceNotFoundException;
import com.flow.booktrade.repository.UserRepository;
import com.flow.booktrade.security.SecurityUtils;
import com.flow.booktrade.service.mapper.UserMapper;
import com.flow.booktrade.service.util.CompareUtil;
import com.flow.booktrade.service.util.RandomUtil;
import com.flow.booktrade.service.util.RestPreconditions;
import com.flow.booktrade.web.rest.vm.ManagedUserVM;
import com.flow.booktrade.web.rest.vm.SignupRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import javax.inject.Inject;
import java.util.*;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService extends BaseService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    public JdbcTokenStore jdbcTokenStore;

    @Inject
    private UserRepository userRepository;

    private UserMapper userMapper = new UserMapper();

    public Optional<RUser> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<RUser> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
            .filter(user -> {
                ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
                return user.getResetDate().isAfter(oneDayAgo);
           })
           .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                return user;
           });
    }

    public Optional<RUser> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(RUser::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(ZonedDateTime.now());
                return user;
            });
    }

    public RUser createUser(String login, String password, String name, String email,
        String langKey) {

        RUser newUser = new RUser();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        newUser.setUserRole(UserRole.USER);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
    
    public User createUserFromSignupRequest(SignupRequest req) throws BadRequestException{
    	RestPreconditions.checkNotNull(req);
    	if(userRepository.findOneByEmail(req.getEmail()).isPresent()){
    		throw new BadRequestException("Email already in use.");
    	} 
    	User user = new User();
    	user.setEmail(req.getEmail());
    	user.setName(req.getName());
    	user.setLogin(req.getEmail());
    	user.setPassword(passwordEncoder.encode(req.getPassword()));
    	user.setActivated(true);
    	user.setRole(UserRole.USER);
    	RUser ru = userMapper.toRUser(user);
    	RUser created = userRepository.save(ru);
    	return userMapper.toUser(created);
    }

    public RUser createUser(ManagedUserVM managedUserVM) {
        RUser user = new RUser();
        user.setLogin(managedUserVM.getLogin());
        user.setName(managedUserVM.getName());
        user.setEmail(managedUserVM.getEmail());
        if (managedUserVM.getLangKey() == null) {
            user.setLangKey("en"); // default language
        } else {
            user.setLangKey(managedUserVM.getLangKey());
        }
        user.setUserRole(managedUserVM.getUserRole());
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(ZonedDateTime.now());
        user.setActivated(true);
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    public void updateUser(String name, String email, String langKey) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            user.setName(name);
            user.setEmail(email);
            user.setLangKey(langKey);
            log.debug("Changed Information for User: {}", user);
        });
    }

    public void updateUser(Long id, String login, String name, String email,
        boolean activated, String langKey, UserRole role) {

        Optional.of(userRepository
            .findOne(id))
            .ifPresent(user -> {
                user.setLogin(login);
                user.setName(name);
                user.setEmail(email);
                user.setActivated(activated);
                user.setLangKey(langKey);
                user.setUserRole(role);
                log.debug("Changed Information for User: {}", user);
            });
    }

    public void deleteUser(String login) {
        jdbcTokenStore.findTokensByUserName(login).forEach(token ->
            jdbcTokenStore.removeAccessToken(token));
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(encryptedPassword);
            log.debug("Changed password for User: {}", user);
        });
    }

    @Transactional(readOnly = true)
    public Optional<RUser> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login).map(user -> {
            return user;
        });
    }
    
    @Transactional(readOnly = true)
    public User getCurrentUserDTOWithAuthorities(){
    	Optional<RUser> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        RUser user = null;
        if (optionalUser.isPresent()) {
          user = optionalUser.get();
         }
         return userMapper.toUser(user);
    }

    @Transactional(readOnly = true)
    public RUser getUserWithAuthorities(Long id) {
        RUser user = userRepository.findOne(id);
        return user;
    }

    @Transactional(readOnly = true)
    public RUser getUserWithAuthorities() {
        Optional<RUser> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        RUser user = null;
        if (optionalUser.isPresent()) {
          user = optionalUser.get();
         }
         return user;
    }
    
    /**
     * Allow for a user to update their avatar
     * @param user
     * @param avatar
     * @return
     * @throws ResourceNotFoundException
     */
    @Transactional
    public User updateAvatar(User user, String avatar) throws ResourceNotFoundException{
    	RestPreconditions.checkNotNull(user);
    	RUser ru = loadUserEntity(user.getId());
    	boolean dirty = false;
    	if(!CompareUtil.compare(ru.getAvatar(), avatar)){
    		ru.setAvatar(avatar);
    		dirty = true;
    	}
    	
    	if(dirty){
    		userRepository.save(ru);
    		user.setAvatar(avatar);
    		return user;
    	}
    	
    	return user;
    	
    }
    
    /**
	 * Find the user's platform
	 * @param userId
	 * @return
	 */
	public Platform findPlatformByUserId(Long userId){
		RestPreconditions.checkNotNull(userId);
		return userRepository.findPlatformByUserId(userId);
	}
	
	public User updateDeviceToken(User user, String deviceToken) throws ResourceNotFoundException{
		RestPreconditions.checkNotNull(user);
		RUser ru = loadUserEntity(user.getId());
		boolean dirty = false;
		if(!CompareUtil.compare(ru.getDeviceToken(), deviceToken)){
			ru.setDeviceToken(deviceToken);
			dirty = true;
		}
		
		if(dirty){
			RUser saved = userRepository.save(ru);
			return userMapper.toUser(saved);
		}
		
		return user;
	}
}
