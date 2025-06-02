package com.erendogan6.planmyworkout.feature.onboarding.util;

import com.erendogan6.planmyworkout.feature.onboarding.model.AiWorkoutPlanResponse;
import com.erendogan6.planmyworkout.feature.onboarding.model.Exercise;
import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AiPlanConverter {

    public static WorkoutPlan convertAiPlanToWorkoutPlan(AiWorkoutPlanResponse aiPlan) {
        String planId = "ai_" + UUID.randomUUID().toString().substring(0, 8);

        WorkoutPlan workoutPlan = new WorkoutPlan(
                planId,
                aiPlan.getName(),
                aiPlan.getDescription(),
                aiPlan.getDifficulty(),
                aiPlan.getDays(),
                aiPlan.getDurationWeeks()
        );

        // Convert AI exercises to regular exercises
        List<Exercise> exercises = convertAiExercisesToExercises(aiPlan.getExercises());
        workoutPlan.setExercises(exercises);

        return workoutPlan;
    }

    private static List<Exercise> convertAiExercisesToExercises(List<AiWorkoutPlanResponse.AiExercise> aiExercises) {
        List<Exercise> exercises = new ArrayList<>();

        for (int i = 0; i < aiExercises.size(); i++) {
            AiWorkoutPlanResponse.AiExercise aiExercise = aiExercises.get(i);

            Exercise exercise = new Exercise(
                    "ex_" + i,
                    aiExercise.getName(),
                    aiExercise.getDescription(),
                    aiExercise.getMuscleGroup(),
                    "", // imageUrl - empty for AI generated exercises
                    3, // default sets
                    10, // default reps
                    60, // default rest seconds
                    "reps" // default unit
            );

            exercises.add(exercise);
        }

        return exercises;
    }
}