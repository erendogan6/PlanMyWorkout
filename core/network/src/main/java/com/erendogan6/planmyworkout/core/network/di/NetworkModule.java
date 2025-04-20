package com.erendogan6.planmyworkout.core.network.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Hilt module for providing network dependencies.
 */
@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    // Network-related dependencies will be added here
    // Firebase dependencies are provided by the app module's FirebaseModule
}
