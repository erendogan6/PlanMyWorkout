package com.erendogan6.planmyworkout.feature.auth.repository;

import com.erendogan6.planmyworkout.feature.auth.model.AuthResult;
import com.erendogan6.planmyworkout.feature.auth.model.AuthResponse;
import com.erendogan6.planmyworkout.feature.auth.model.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of the AuthRepository interface using Firebase Authentication.
 * Handles all Firebase authentication operations and converts Firebase objects to domain models.
 */
@Singleton
public class AuthRepositoryImpl implements AuthRepository {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    /**
     * Constructor for dependency injection.
     *
     * @param firebaseAuth Firebase Authentication instance
     * @param firestore Firebase Firestore instance
     */
    @Inject
    public AuthRepositoryImpl(FirebaseAuth firebaseAuth, FirebaseFirestore firestore) {
        this.firebaseAuth = firebaseAuth;
        this.firestore = firestore;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Authenticates a user using Firebase Authentication and converts the result to a domain model.
     */
    @Override
    public Task<AuthResponse<AuthResult>> login(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password)
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        if (firebaseUser != null) {
                            User user = convertFirebaseUserToUser(firebaseUser);
                            return new AuthResponse.Success<>(new AuthResult(user));
                        } else {
                            return new AuthResponse.Error<>("User data not found after login", null);
                        }
                    } else {
                        Exception exception = task.getException();
                        return new AuthResponse.Error<>(getErrorMessage(exception, "Login failed"), exception);
                    }
                });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Creates a new user account using Firebase Authentication and converts the result to a domain model.
     */
    @Override
    public Task<AuthResponse<AuthResult>> register(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        if (firebaseUser != null) {
                            User user = convertFirebaseUserToUser(firebaseUser);
                            return new AuthResponse.Success<>(new AuthResult(user));
                        } else {
                            return new AuthResponse.Error<>("User data not found after registration", null);
                        }
                    } else {
                        Exception exception = task.getException();
                        return new AuthResponse.Error<>(getErrorMessage(exception, "Registration failed"), exception);
                    }
                });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Uses Firebase Authentication to send a password reset email.
     */
    @Override
    public Task<AuthResponse<Void>> resetPassword(String email) {
        return firebaseAuth.sendPasswordResetEmail(email)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return new AuthResponse.Success<>(null);
                    } else {
                        Exception exception = task.getException();
                        return new AuthResponse.Error<>(getErrorMessage(exception, "Failed to send password reset email"), exception);
                    }
                });
    }

    /**
     * Gets a user-friendly error message based on the Firebase exception type.
     *
     * @param exception The Firebase exception
     * @param defaultMessage Default message if the exception type is unknown
     * @return A user-friendly error message
     */
    private String getErrorMessage(Exception exception, String defaultMessage) {
        if (exception == null) {
            return defaultMessage;
        }

        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return "Invalid email or password";
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            return "User account not found";
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            return "Email is already in use";
        } else {
            return exception.getMessage() != null ? exception.getMessage() : defaultMessage;
        }
    }

    /**
     * {@inheritDoc}
     *
     * Signs out the current user from Firebase Authentication.
     */
    @Override
    public void signOut() {
        firebaseAuth.signOut();
    }

    /**
     * {@inheritDoc}
     *
     * Checks if there is a currently authenticated user in Firebase.
     */
    @Override
    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    /**
     * {@inheritDoc}
     *
     * Gets the current user from Firebase and converts it to a domain model.
     */
    @Override
    public User getCurrentUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        return firebaseUser != null ? convertFirebaseUserToUser(firebaseUser) : null;
    }

    /**
     * {@inheritDoc}
     *
     * Gets the ID of the currently authenticated user from Firebase.
     */
    @Override
    public String getCurrentUserId() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser != null ? currentUser.getUid() : null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Checks if the user has completed onboarding by looking for a mainPlanId in Firestore.
     */
    @Override
    public Task<Boolean> hasCompletedOnboarding() {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Tasks.forResult(false);
        }

        return firestore.collection("users")
                .document(userId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Check if the user has a mainPlanId or any plans
                            if (document.contains("mainPlanId") && document.getString("mainPlanId") != null) {
                                return true;
                            }

                            // Alternatively, check if the user has completed onboarding
                            if (document.contains("onboardingCompleted")) {
                                Boolean onboardingCompleted = document.getBoolean("onboardingCompleted");
                                return onboardingCompleted != null && onboardingCompleted;
                            }
                        }
                    }
                    return false;
                });
    }

    /**
     * Converts a FirebaseUser object to the application's User model.
     *
     * @param firebaseUser The Firebase user object to convert
     * @return A User object containing the relevant information
     */
    private User convertFirebaseUserToUser(FirebaseUser firebaseUser) {
        return new User(
                firebaseUser.getUid(),
                firebaseUser.getEmail(),
                firebaseUser.getDisplayName(),
                firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null
        );
    }
}