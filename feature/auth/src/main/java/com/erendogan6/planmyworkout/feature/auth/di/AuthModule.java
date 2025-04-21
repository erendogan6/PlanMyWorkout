package com.erendogan6.planmyworkout.feature.auth.di;

import android.content.Context;

import com.erendogan6.planmyworkout.feature.auth.repository.AuthRepository;
import com.erendogan6.planmyworkout.feature.auth.repository.AuthRepositoryImpl;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
 * Dagger module for providing authentication-related dependencies.
 */
@Module
@InstallIn(SingletonComponent.class)
public class AuthModule {

    /**
     * Provides FirebaseAuth instance.
     *
     * @param context Application context
     * @return FirebaseAuth instance
     */
    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuth(@ApplicationContext Context context) {
        // Ensure Firebase is initialized
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context);
        }
        return FirebaseAuth.getInstance();
    }

    // FirebaseFirestore is provided by CoreModule

    /**
     * Provides AuthRepository implementation.
     *
     * @param firebaseAuth Firebase authentication instance
     * @return AuthRepository implementation
     */
    @Provides
    @Singleton
    public AuthRepository provideAuthRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firestore) {
        return new AuthRepositoryImpl(firebaseAuth, firestore);
    }
}