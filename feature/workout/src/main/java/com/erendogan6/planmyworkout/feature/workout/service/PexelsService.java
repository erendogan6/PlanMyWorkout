package com.erendogan6.planmyworkout.feature.workout.service;

import com.erendogan6.planmyworkout.feature.workout.model.PexelsPhotoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface PexelsService {
    @GET("v1/search")
    Call<PexelsPhotoResponse> searchPhotos(
            @Header("Authorization") String apiKey,
            @Query("query") String query,
            @Query("per_page") int perPage
    );
}