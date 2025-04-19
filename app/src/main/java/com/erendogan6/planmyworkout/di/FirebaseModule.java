package com.erendogan6.planmyworkout.di;

import android.content.Context;

import com.erendogan6.planmyworkout.data.repository.AuthRepositoryImpl;
import com.erendogan6.planmyworkout.domain.repository.AuthRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import dagger.hilt.android.qualifiers.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Hilt module for providing Firebase dependencies.
 */
@Module
@InstallIn(SingletonComponent.class)
public class FirebaseModule {

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

    /**
     * Provides FirebaseFirestore instance.
     *
     * @param context Application context
     * @return FirebaseFirestore instance
     */
    @Provides
    @Singleton
    public FirebaseFirestore provideFirebaseFirestore(@ApplicationContext Context context) {
        // Ensure Firebase is initialized
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context);
        }
        return FirebaseFirestore.getInstance();
    }

    /**
     * Provides AuthRepository implementation.
     * Using real AuthRepositoryImpl since we have a Firebase project set up.
     *
     * @param firebaseAuth FirebaseAuth instance
     * @return AuthRepository implementation
     */
    @Provides
    @Singleton
    public AuthRepository provideAuthRepository(FirebaseAuth firebaseAuth) {
        return new AuthRepositoryImpl(firebaseAuth);
    }
}
