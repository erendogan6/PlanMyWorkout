package com.erendogan6.planmyworkout.feature.auth.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository for authentication operations.
 */
@Singleton
public class AuthRepository {
    
    private final FirebaseAuth firebaseAuth;
    
    @Inject
    public AuthRepository(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }
    
    /**
     * Login with email and password.
     *
     * @param email    User's email
     * @param password User's password
     * @return Task with the authentication result
     */
    public Task<AuthResult> login(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }
    
    /**
     * Register a new user with email and password.
     *
     * @param email    User's email
     * @param password User's password
     * @return Task with the authentication result
     */
    public Task<AuthResult> register(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }
    
    /**
     * Send a password reset email.
     *
     * @param email User's email
     * @return Task with the result
     */
    public Task<Void> resetPassword(String email) {
        return firebaseAuth.sendPasswordResetEmail(email);
    }
    
    /**
     * Sign out the current user.
     */
    public void signOut() {
        firebaseAuth.signOut();
    }
    
    /**
     * Check if a user is currently logged in.
     *
     * @return True if a user is logged in, false otherwise
     */
    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }
    
    /**
     * Get the current user.
     *
     * @return The current FirebaseUser or null if not logged in
     */
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }
}
