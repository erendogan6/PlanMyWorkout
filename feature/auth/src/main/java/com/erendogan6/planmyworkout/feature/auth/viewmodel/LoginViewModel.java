package com.erendogan6.planmyworkout.feature.auth.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.core.model.Result;
import com.erendogan6.planmyworkout.feature.auth.data.AuthRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the login screen.
 */
@HiltViewModel
public class LoginViewModel extends ViewModel {
    
    private final AuthRepository authRepository;
    private final MutableLiveData<Result<Boolean>> loginResult = new MutableLiveData<>();
    
    @Inject
    public LoginViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    
    /**
     * Attempt to log in with the provided credentials.
     *
     * @param email    User's email
     * @param password User's password
     */
    public void login(String email, String password) {
        loginResult.setValue(Result.loading(false));
        
        authRepository.login(email, password)
                .addOnSuccessListener(authResult -> loginResult.setValue(Result.success(true)))
                .addOnFailureListener(e -> loginResult.setValue(Result.error(e.getMessage(), false)));
    }
    
    /**
     * Get the login result LiveData.
     *
     * @return LiveData with the login result
     */
    public LiveData<Result<Boolean>> getLoginResult() {
        return loginResult;
    }
}
