package com.erendogan6.planmyworkout.domain.usecase.auth;

/**
 * Parameters for the register use case.
 */
public class RegisterParams {
    private final String email;
    private final String password;
    
    public RegisterParams(String email, String password) {
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
