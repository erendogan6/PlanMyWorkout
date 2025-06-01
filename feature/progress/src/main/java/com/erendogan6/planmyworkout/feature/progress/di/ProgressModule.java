package com.erendogan6.planmyworkout.feature.progress.di;

import com.erendogan6.planmyworkout.feature.progress.repository.ProgressRepository;
import com.erendogan6.planmyworkout.feature.progress.repository.ProgressRepositoryImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Dagger module for providing progress-related dependencies.
 */
@Module
@InstallIn(SingletonComponent.class)
public class ProgressModule {

    @Provides
    @Singleton
    public ProgressRepository provideProgressRepository(FirebaseFirestore firestore, FirebaseAuth firebaseAuth) {
        return new ProgressRepositoryImpl(firestore, firebaseAuth);
    }
}