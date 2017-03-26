package com.flow.booktrade.dto;

import com.flow.booktrade.config.Constants;

import com.flow.booktrade.domain.RUser;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String name;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    private String langKey;

    private UserRole role;

    public UserDTO() {
    }

    public UserDTO(RUser user) {
        this(user.getLogin(), user.getName(),
            user.getEmail(), user.getActivated(), user.getLangKey(),
            user.getUserRole());
    }

    public UserDTO(String login, String name,
        String email, boolean activated, String langKey, UserRole role) {

        this.login = login;
        this.name = name;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public UserRole getUserRole(){
    	return role;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + name + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", role=" + role +
            "}";
    }
}
