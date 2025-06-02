package com.erendogan6.planmyworkout;

import android.app.Application;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import dagger.hilt.android.HiltAndroidApp;

/**
 * Main application class for PlanMyWorkout.
 */
@HiltAndroidApp
public class PlanMyWorkoutApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initializeRemoteConfig();
    }

    private void initializeRemoteConfig() {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.fetchAndActivate();
    }
}