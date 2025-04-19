package com.erendogan6.planmyworkout.domain.usecase.auth;

/**
 * Parameters for the reset password use case.
 */
public class ResetPasswordParams {
    private final String email;
    
    public ResetPasswordParams(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
}
