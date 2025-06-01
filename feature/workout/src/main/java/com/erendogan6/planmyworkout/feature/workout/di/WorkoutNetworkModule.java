package com.erendogan6.planmyworkout.feature.workout.di;

import com.erendogan6.planmyworkout.feature.workout.service.PexelsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class WorkoutNetworkModule {

    @Provides
    @Singleton
    PexelsService providePexelsService() {
        return new Retrofit.Builder()
                .baseUrl("https://api.pexels.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PexelsService.class);
    }
}