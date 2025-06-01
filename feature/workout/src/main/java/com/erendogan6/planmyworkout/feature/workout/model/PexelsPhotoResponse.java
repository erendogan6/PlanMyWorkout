package com.erendogan6.planmyworkout.feature.workout.model;

import java.util.List;

public class PexelsPhotoResponse {
    private List<Photo> photos;

    public PexelsPhotoResponse() {}

    public PexelsPhotoResponse(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
