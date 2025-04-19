package com.erendogan6.planmyworkout.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.repository.AuthRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of AuthRepository using Firebase Authentication.
 * This class is part of the data layer and provides concrete implementation of authentication operations.
 */
@Singleton
public class FirebaseAuthRepository implements AuthRepository {
    
    private final FirebaseAuth firebaseAuth;
    
    @Inject
    public FirebaseAuthRepository(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }
    
    @Override
    public LiveData<Result<Boolean>> login(String email, String password) {
        MutableLiveData<Result<Boolean>> resultLiveData = new MutableLiveData<>();
        
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            resultLiveData.setValue(Result.error("Email and password cannot be empty", false));
            return resultLiveData;
        }
        
        resultLiveData.setValue(Result.loading(false));
        
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
                resultLiveData.setValue(Result.success(true));
            })
            .addOnFailureListener(e -> {
                String errorMessage = "Login failed";
                
                if (e instanceof FirebaseAuthInvalidUserException) {
                    errorMessage = "User does not exist";
                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    errorMessage = "Invalid credentials";
                }
                
                resultLiveData.setValue(Result.error(errorMessage, false));
            });
        
        return resultLiveData;
    }
    
    @Override
    public LiveData<Result<Boolean>> register(String email, String password) {
        MutableLiveData<Result<Boolean>> resultLiveData = new MutableLiveData<>();
        
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            resultLiveData.setValue(Result.error("Email and password cannot be empty", false));
            return resultLiveData;
        }
        
        resultLiveData.setValue(Result.loading(false));
        
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
                resultLiveData.setValue(Result.success(true));
            })
            .addOnFailureListener(e -> {
                String errorMessage = "Registration failed: " + e.getMessage();
                resultLiveData.setValue(Result.error(errorMessage, false));
            });
        
        return resultLiveData;
    }
    
    @Override
    public LiveData<Result<Boolean>> resetPassword(String email) {
        MutableLiveData<Result<Boolean>> resultLiveData = new MutableLiveData<>();
        
        if (email == null || email.isEmpty()) {
            resultLiveData.setValue(Result.error("Email cannot be empty", false));
            return resultLiveData;
        }
        
        resultLiveData.setValue(Result.loading(false));
        
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener(unused -> {
                resultLiveData.setValue(Result.success(true));
            })
            .addOnFailureListener(e -> {
                String errorMessage = "Password reset failed: " + e.getMessage();
                resultLiveData.setValue(Result.error(errorMessage, false));
            });
        
        return resultLiveData;
    }
    
    @Override
    public boolean isLoggedIn() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser != null;
    }
    
    @Override
    public void signOut() {
        firebaseAuth.signOut();
    }
}
