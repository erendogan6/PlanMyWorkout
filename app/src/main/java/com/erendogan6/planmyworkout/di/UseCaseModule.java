package com.erendogan6.planmyworkout.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Hilt module for providing use case dependencies.
 */
@Module
@InstallIn(SingletonComponent.class)
public class UseCaseModule {
    // Use cases are now provided by their respective feature modules
}
