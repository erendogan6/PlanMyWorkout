package com.erendogan6.planmyworkout.core.di;

import android.content.Context;
import android.content.SharedPreferences;

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

    /**
     * Provides SharedPreferences instance.
     *
     * @param context Application context
     * @return SharedPreferences instance
     */
    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(@ApplicationContext Context context) {
        return context.getSharedPreferences("plan_my_workout_prefs", Context.MODE_PRIVATE);
    }
}
