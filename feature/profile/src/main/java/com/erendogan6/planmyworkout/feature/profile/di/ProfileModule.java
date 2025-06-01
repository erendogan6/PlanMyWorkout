package com.erendogan6.planmyworkout.feature.profile.di;

import com.erendogan6.planmyworkout.feature.profile.repository.ProfileRepository;
import com.erendogan6.planmyworkout.feature.profile.repository.ProfileRepositoryImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Dagger module for providing profile-related dependencies.
 */
@Module
@InstallIn(SingletonComponent.class)
public class ProfileModule {

    /**
     * Provides ProfileRepository implementation.
     *
     * @param firebaseAuth Firebase authentication instance
     * @param firestore    Firebase Firestore instance
     * @return ProfileRepository implementation
     */
    @Provides
    @Singleton
    public ProfileRepository provideProfileRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firestore) {
        return new ProfileRepositoryImpl(firebaseAuth, firestore);
    }
}