package com.erendogan6.planmyworkout.feature.onboarding.data;

import com.erendogan6.planmyworkout.core.util.FirestoreManager;
import com.erendogan6.planmyworkout.feature.onboarding.model.OnboardingChoice;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository for onboarding-related operations.
 */
@Singleton
public class OnboardingRepository {

    private final FirestoreManager firestoreManager;

    @Inject
    public OnboardingRepository(FirestoreManager firestoreManager) {
        this.firestoreManager = firestoreManager;
    }

    /**
     * Save the user's onboarding choice to Firestore.
     *
     * @param choice The selected onboarding choice
     * @return LiveData with the result of the save operation
     */
    public androidx.lifecycle.LiveData<com.erendogan6.planmyworkout.core.model.Result<Boolean>> saveOnboardingChoice(OnboardingChoice choice) {
        String userId = firestoreManager.getCurrentUserId();
        if (userId != null) {
            // Create a Map to store the choice
            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("choice", choice.name());

            return firestoreManager.saveUserData(userId, "settings", "onboarding", data);
        }

        // Return error if user ID is null
        androidx.lifecycle.MutableLiveData<com.erendogan6.planmyworkout.core.model.Result<Boolean>> result = new androidx.lifecycle.MutableLiveData<>();
        result.setValue(com.erendogan6.planmyworkout.core.model.Result.error("User not logged in", false));
        return result;
    }
}
