package com.erendogan6.planmyworkout.core.di;

import android.content.Context;

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
 * Hilt module for providing core dependencies.
 */
@Module
@InstallIn(SingletonComponent.class)
public class CoreModule {

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
}
