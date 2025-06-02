package com.erendogan6.planmyworkout.feature.onboarding.model;

public class AiWorkoutPlanRequest {
    private final String goal;
    private final String experienceLevel;
    private final int daysPerWeek;
    private final int sessionDuration;
    private final String equipment;
    private final String limitations;
    private final String focusAreas;

    public AiWorkoutPlanRequest(String goal, String experienceLevel, int daysPerWeek,
                                int sessionDuration, String equipment, String limitations,
                                String focusAreas) {
        this.goal = goal;
        this.experienceLevel = experienceLevel;
        this.daysPerWeek = daysPerWeek;
        this.sessionDuration = sessionDuration;
        this.equipment = equipment;
        this.limitations = limitations;
        this.focusAreas = focusAreas;
    }

    // Getters
    public String getGoal() { return goal; }
    public String getExperienceLevel() { return experienceLevel; }
    public int getDaysPerWeek() { return daysPerWeek; }
    public int getSessionDuration() { return sessionDuration; }
    public String getEquipment() { return equipment; }
    public String getLimitations() { return limitations; }
    public String getFocusAreas() { return focusAreas; }
}