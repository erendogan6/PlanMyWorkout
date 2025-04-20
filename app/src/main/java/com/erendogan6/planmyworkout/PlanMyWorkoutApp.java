package com.erendogan6.planmyworkout;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import dagger.hilt.android.HiltAndroidApp;

/**
 * Application class for PlanMyWorkout app.
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection.
 */
@HiltAndroidApp
public class PlanMyWorkoutApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
    }
}
