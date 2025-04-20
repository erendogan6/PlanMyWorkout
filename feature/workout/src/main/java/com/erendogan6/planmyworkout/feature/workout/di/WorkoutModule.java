package com.erendogan6.planmyworkout.feature.workout.di;

import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;
import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Dagger module for providing workout dependencies.
 */
@Module
@InstallIn(SingletonComponent.class)
public abstract class WorkoutModule {

    @Binds
    abstract WorkoutRepository bindWorkoutRepository(WorkoutRepositoryImpl impl);
}