package com.erendogan6.planmyworkout.core.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.erendogan6.planmyworkout.core.model.Result;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manager class for Firestore operations.
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
     * Get the current user ID.
     *
     * @return The current user ID or null if not logged in.
     */
    public String getCurrentUserId() {
        if (firebaseAuth.getCurrentUser() != null) {
            return firebaseAuth.getCurrentUser().getUid();
        }
        return null;
    }

    /**
     * Save data to Firestore.
     *
     * @param collection Collection name
     * @param documentId Document ID
     * @param data       Data to save
     * @param <T>        Type of data
     * @return LiveData with result
     */
    public <T> LiveData<Result<Boolean>> saveData(String collection, String documentId, T data) {
        MutableLiveData<Result<Boolean>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading(false));

        firestore.collection(collection)
                .document(documentId)
                .set(data)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success(true)))
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage(), false)));

        return resultLiveData;
    }

    /**
     * Save data to a user's subcollection.
     *
     * @param userId       User ID
     * @param subcollection Subcollection name
     * @param documentId   Document ID
     * @param data         Data to save
     * @param <T>          Type of data
     * @return LiveData with result
     */
    public <T> LiveData<Result<Boolean>> saveUserData(String userId, String subcollection, String documentId, T data) {
        MutableLiveData<Result<Boolean>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading(false));

        firestore.collection("users")
                .document(userId)
                .collection(subcollection)
                .document(documentId)
                .set(data)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success(true)))
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage(), false)));

        return resultLiveData;
    }
}
