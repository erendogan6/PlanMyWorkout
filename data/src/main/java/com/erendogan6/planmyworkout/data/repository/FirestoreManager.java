package com.erendogan6.planmyworkout.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.erendogan6.planmyworkout.domain.model.OnboardingChoice;
import com.erendogan6.planmyworkout.domain.model.Result;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manager class for Firestore operations.
 * This class is part of the data layer and provides methods for interacting with Firestore.
 */
@Singleton
public class FirestoreManager {

    private final FirebaseFirestore firestore;
    private final FirebaseAuth firebaseAuth;

    @Inject
    public FirestoreManager(FirebaseFirestore firestore, FirebaseAuth firebaseAuth) {
        this.firestore = firestore;
        this.firebaseAuth = firebaseAuth;
    }

    /**
     * Save the user's onboarding choice to Firestore.
     *
     * @param choice The user's onboarding choice
     * @return LiveData emitting Result of the save operation
     */
    public LiveData<Result<Boolean>> saveOnboardingChoice(OnboardingChoice choice) {
        MutableLiveData<Result<Boolean>> resultLiveData = new MutableLiveData<>();

        // Check if choice is null
        if (choice == null) {
            Log.e("FirestoreManager", "Choice is null");
            resultLiveData.setValue(Result.error("Choice cannot be null", false));
            return resultLiveData;
        }

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e("FirestoreManager", "User not logged in");
            resultLiveData.setValue(Result.error("User not logged in", false));
            return resultLiveData;
        }

        Log.d("FirestoreManager", "Saving onboarding choice: " + choice.name() + " for user: " + currentUser.getUid());

        resultLiveData.setValue(Result.loading(false));

        DocumentReference userRef = firestore.collection("users").document(currentUser.getUid());

        // First check if the document exists
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Document exists, update it
                    Log.d("FirestoreManager", "Document exists, updating it");
                    userRef.update("onboardingChoice", choice.name())
                        .addOnSuccessListener(aVoid -> {
                            Log.d("FirestoreManager", "Successfully updated onboarding choice");
                            resultLiveData.setValue(Result.success(true));
                        })
                        .addOnFailureListener(e -> {
                            Log.e("FirestoreManager", "Error updating document: " + e.getMessage());
                            resultLiveData.setValue(Result.error("Error saving choice: " + e.getMessage(), false));
                        });
                } else {
                    // Document doesn't exist, create it
                    Log.d("FirestoreManager", "Document doesn't exist, creating it");
                    userRef.set(new UserData(choice.name()))
                        .addOnSuccessListener(aVoid -> {
                            Log.d("FirestoreManager", "Successfully created document with onboarding choice");
                            resultLiveData.setValue(Result.success(true));
                        })
                        .addOnFailureListener(e -> {
                            Log.e("FirestoreManager", "Error creating document: " + e.getMessage());
                            resultLiveData.setValue(Result.error("Error saving choice: " + e.getMessage(), false));
                        });
                }
            } else {
                // Error getting document
                Log.e("FirestoreManager", "Error getting document: " + task.getException().getMessage());
                resultLiveData.setValue(Result.error("Error getting document: " + task.getException().getMessage(), false));
            }
        });

        return resultLiveData;
    }

    /**
     * Check if the user has completed onboarding.
     *
     * @return LiveData emitting Result containing the user's onboarding choice
     */
    public LiveData<Result<OnboardingChoice>> getOnboardingChoice() {
        MutableLiveData<Result<OnboardingChoice>> resultLiveData = new MutableLiveData<>();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            resultLiveData.setValue(Result.error("User not logged in", null));
            return resultLiveData;
        }

        resultLiveData.setValue(Result.loading(null));

        firestore.collection("users").document(currentUser.getUid())
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists() && documentSnapshot.contains("onboardingChoice")) {
                    String choiceStr = documentSnapshot.getString("onboardingChoice");
                    try {
                        OnboardingChoice choice = OnboardingChoice.valueOf(choiceStr);
                        resultLiveData.setValue(Result.success(choice));
                    } catch (IllegalArgumentException e) {
                        resultLiveData.setValue(Result.error("Invalid onboarding choice", null));
                    }
                } else {
                    resultLiveData.setValue(Result.success(null));
                }
            })
            .addOnFailureListener(e -> {
                resultLiveData.setValue(Result.error("Error getting onboarding choice: " + e.getMessage(), null));
            });

        return resultLiveData;
    }

    /**
     * Helper class for user data.
     */
    private static class UserData {
        private final String onboardingChoice;

        public UserData(String onboardingChoice) {
            this.onboardingChoice = onboardingChoice;
        }

        public String getOnboardingChoice() {
            return onboardingChoice;
        }
    }
}
