package com.erendogan6.planmyworkout.feature.onboarding.model;

public class ExerciseTemplate {
    private String name;
    private String description;
    private String muscleGroup;
    private String imageUrl;
    private int defaultSets;
    private int defaultReps;
    private int defaultRestSeconds;
    private String unit;

    public ExerciseTemplate() {}

    public ExerciseTemplate(String name, String description, String muscleGroup,
                            int defaultSets, int defaultReps, int defaultRestSeconds) {
        this.name = name;
        this.description = description;
        this.muscleGroup = muscleGroup;
        this.defaultSets = defaultSets;
        this.defaultReps = defaultReps;
        this.defaultRestSeconds = defaultRestSeconds;
        this.unit = "reps";
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getDefaultSets() { return defaultSets; }
    public void setDefaultSets(int defaultSets) { this.defaultSets = defaultSets; }

    public int getDefaultReps() { return defaultReps; }
    public void setDefaultReps(int defaultReps) { this.defaultReps = defaultReps; }

    public int getDefaultRestSeconds() { return defaultRestSeconds; }
    public void setDefaultRestSeconds(int defaultRestSeconds) { this.defaultRestSeconds = defaultRestSeconds; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Exercise toExercise(String exerciseId) {
        return new Exercise(
                exerciseId,
                name,
                description,
                muscleGroup,
                imageUrl != null ? imageUrl : "",
                defaultSets,
                defaultReps,
                defaultRestSeconds,
                unit
        );
    }
}