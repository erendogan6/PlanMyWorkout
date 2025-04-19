package com.erendogan6.planmyworkout.domain.usecase.auth;

/**
 * Parameters for the login use case.
 */
public class LoginParams {
    private final String email;
    private final String password;
    
    public LoginParams(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
}
