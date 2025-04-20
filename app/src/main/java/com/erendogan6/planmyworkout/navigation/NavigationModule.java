package com.erendogan6.planmyworkout.navigation;

import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Hilt module for providing navigation dependencies.
 */
@Module
@InstallIn(SingletonComponent.class)
public class NavigationModule {
    // This module will be used to provide navigation-related dependencies
    // such as NavigationController, DeepLinkHandler, etc.
}
