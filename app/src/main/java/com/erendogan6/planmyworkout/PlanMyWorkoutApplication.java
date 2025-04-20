package com.erendogan6.planmyworkout;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

/**
 * Main application class for PlanMyWorkout.
 */
@HiltAndroidApp
public class PlanMyWorkoutApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize any application-wide components here
    }
}
