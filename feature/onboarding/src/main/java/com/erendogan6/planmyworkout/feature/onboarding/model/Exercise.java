package com.erendogan6.planmyworkout.feature.onboarding.model;

import java.io.Serializable;

/**
 * Model class representing an exercise.
 */
public class Exercise implements Serializable {
    private String id;
    private String name;
    private String description;
    private String muscleGroup;
    private String imageUrl;
    private int sets;
    private int repsPerSet;
    private int restSeconds;

    public Exercise() {
        // Required empty constructor for Firestore
    }

    public Exercise(String id, String name, String description, String muscleGroup, 
                   String imageUrl, int sets, int repsPerSet, int restSeconds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.muscleGroup = muscleGroup;
        this.imageUrl = imageUrl;
        this.sets = sets;
        this.repsPerSet = repsPerSet;
        this.restSeconds = restSeconds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getRepsPerSet() {
        return repsPerSet;
    }

    public void setRepsPerSet(int repsPerSet) {
        this.repsPerSet = repsPerSet;
    }

    public int getRestSeconds() {
        return restSeconds;
    }

    public void setRestSeconds(int restSeconds) {
        this.restSeconds = restSeconds;
    }
}
