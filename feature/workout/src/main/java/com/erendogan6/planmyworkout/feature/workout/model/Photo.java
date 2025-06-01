package com.erendogan6.planmyworkout.feature.workout.model;

public class Photo {
    private PhotoSrc src;

    public Photo() {}

    public Photo(PhotoSrc src) {
        this.src = src;
    }

    public PhotoSrc getSrc() {
        return src;
    }

    public void setSrc(PhotoSrc src) {
        this.src = src;
    }
}
