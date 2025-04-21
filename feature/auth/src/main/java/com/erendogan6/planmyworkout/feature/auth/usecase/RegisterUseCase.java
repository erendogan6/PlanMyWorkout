package com.erendogan6.planmyworkout.feature.auth.usecase;

import com.erendogan6.planmyworkout.feature.auth.model.AuthResponse;
import com.erendogan6.planmyworkout.feature.auth.model.AuthResult;
import com.erendogan6.planmyworkout.feature.auth.repository.AuthRepository;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use case for registering a new user.
 * Handles the registration process and saving user data to Firestore.
 */
@Singleton
public class RegisterUseCase {

    private final AuthRepository authRepository;
    private final FirebaseFirestore firestore;

    /**
     * Constructor for dependency injection.
     *
     * @param authRepository The authentication repository
     * @param firestore      The Firestore instance
     */
    @Inject
    public RegisterUseCase(AuthRepository authRepository, FirebaseFirestore firestore) {
        this.authRepository = authRepository;
        this.firestore = firestore;
    }

    /**
     * Registers a new user with the provided credentials and saves their profile data to Firestore.
     *
     * @param fullName The user's full name
     * @param email    The user's email address
     * @param password The user's password
     * @return Task with the registration result
     */
    public Task<AuthResponse<AuthResult>> execute(String fullName, String email, String password) {
        return authRepository.register(email, password)
                .continueWithTask(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        AuthResponse<AuthResult> response = task.getResult();
                        
                        if (response.isSuccess() && response.getData() != null) {
                            // Save user data to Firestore
                            String userId = response.getData().getUser().getUid();
                            return saveUserData(userId, fullName, email)
                                    .continueWith(saveTask -> {
                                        if (saveTask.isSuccessful()) {
                                            return response;
                                        } else {
                                            // If saving to Firestore fails, still return success for the auth part
                                            // but log the error or handle it as needed
                                            return response;
                                        }
                                    });
                        } else {
                            return Tasks.forResult(response);
                        }
                    } else {
                        // If registration fails, propagate the error
                        Exception exception = task.getException();
                        return Tasks.forResult(new AuthResponse.Error<>(
                                exception != null ? exception.getMessage() : "Registration failed",
                                exception));
                    }
                });
    }

    /**
     * Saves the user's profile data to Firestore.
     *
     * @param userId   The user's ID
     * @param fullName The user's full name
     * @param email    The user's email address
     * @return Task indicating success or failure
     */
    private Task<Void> saveUserData(String userId, String fullName, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("fullName", fullName);
        userData.put("email", email);
        userData.put("createdAt", new Date());

        return firestore.collection("users")
                .document(userId)
                .set(userData);
    }
}
