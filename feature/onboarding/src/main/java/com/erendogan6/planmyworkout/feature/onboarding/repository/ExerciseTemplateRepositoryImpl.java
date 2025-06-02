package com.erendogan6.planmyworkout.feature.onboarding.repository;

import com.erendogan6.planmyworkout.feature.onboarding.model.ExerciseTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ExerciseTemplateRepositoryImpl implements ExerciseTemplateRepository {

    private List<ExerciseTemplate> exerciseTemplates;

    @Inject
    public ExerciseTemplateRepositoryImpl() {
        initializeExerciseTemplates();
    }

    private void initializeExerciseTemplates() {
        exerciseTemplates = Arrays.asList(
                // Chest Exercises
                new ExerciseTemplate("Push-ups", "Classic bodyweight chest exercise", "Chest", 3, 12, 60),
                new ExerciseTemplate("Bench Press", "Barbell chest press on bench", "Chest", 4, 8, 90),
                new ExerciseTemplate("Incline Dumbbell Press", "Upper chest focused dumbbell press", "Chest", 3, 10, 75),
                new ExerciseTemplate("Chest Dips", "Parallel bar dips for lower chest", "Chest", 3, 10, 60),
                new ExerciseTemplate("Chest Flyes", "Dumbbell flyes for chest isolation", "Chest", 3, 12, 60),

                // Back Exercises
                new ExerciseTemplate("Pull-ups", "Vertical pulling bodyweight exercise", "Back", 3, 8, 90),
                new ExerciseTemplate("Barbell Rows", "Horizontal pulling with barbell", "Back", 4, 8, 75),
                new ExerciseTemplate("Lat Pulldowns", "Machine-based lat exercise", "Back", 3, 12, 60),
                new ExerciseTemplate("Dumbbell Rows", "Single-arm dumbbell rowing", "Back", 3, 10, 60),
                new ExerciseTemplate("Deadlifts", "Full-body posterior chain exercise", "Back", 3, 5, 120),

                // Legs Exercises
                new ExerciseTemplate("Squats", "Bodyweight or barbell squats", "Legs", 4, 12, 90),
                new ExerciseTemplate("Lunges", "Walking or stationary lunges", "Legs", 3, 12, 60),
                new ExerciseTemplate("Leg Press", "Machine-based leg exercise", "Legs", 3, 15, 75),
                new ExerciseTemplate("Calf Raises", "Standing or seated calf raises", "Legs", 4, 15, 45),
                new ExerciseTemplate("Romanian Deadlifts", "Hamstring-focused deadlift variation", "Legs", 3, 10, 75),

                // Shoulders Exercises
                new ExerciseTemplate("Overhead Press", "Standing or seated shoulder press", "Shoulders", 3, 8, 75),
                new ExerciseTemplate("Lateral Raises", "Side deltoid isolation", "Shoulders", 3, 12, 45),
                new ExerciseTemplate("Front Raises", "Front deltoid isolation", "Shoulders", 3, 12, 45),
                new ExerciseTemplate("Rear Delt Flyes", "Posterior deltoid exercise", "Shoulders", 3, 15, 45),
                new ExerciseTemplate("Shrugs", "Trapezius strengthening", "Shoulders", 3, 12, 60),

                // Arms Exercises
                new ExerciseTemplate("Bicep Curls", "Bicep isolation with dumbbells", "Arms", 3, 12, 45),
                new ExerciseTemplate("Tricep Dips", "Tricep bodyweight exercise", "Arms", 3, 12, 60),
                new ExerciseTemplate("Hammer Curls", "Neutral grip bicep exercise", "Arms", 3, 12, 45),
                new ExerciseTemplate("Tricep Extensions", "Overhead tricep isolation", "Arms", 3, 12, 45),
                new ExerciseTemplate("Close-Grip Push-ups", "Tricep-focused push-up variation", "Arms", 3, 10, 60),

                // Core Exercises
                new ExerciseTemplate("Plank", "Isometric core exercise", "Core", 3, 30, 30),
                new ExerciseTemplate("Crunches", "Basic abdominal exercise", "Core", 3, 20, 30),
                new ExerciseTemplate("Russian Twists", "Oblique strengthening exercise", "Core", 3, 20, 30),
                new ExerciseTemplate("Mountain Climbers", "Dynamic core and cardio", "Core", 3, 30, 45),
                new ExerciseTemplate("Dead Bug", "Core stability exercise", "Core", 3, 10, 45),

                // Cardio Exercises
                new ExerciseTemplate("Jumping Jacks", "Full-body cardio exercise", "Cardio", 3, 30, 30),
                new ExerciseTemplate("Burpees", "High-intensity full-body exercise", "Cardio", 3, 10, 60),
                new ExerciseTemplate("High Knees", "Running in place variation", "Cardio", 3, 30, 30),
                new ExerciseTemplate("Jump Squats", "Explosive leg exercise", "Cardio", 3, 15, 45)
        );
    }

    @Override
    public void getExerciseTemplates(ExerciseTemplatesCallback callback) {
        // Simulate async operation
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
            callback.onSuccess(new ArrayList<>(exerciseTemplates));
        });
    }

    @Override
    public void getExerciseTemplatesByMuscleGroup(String muscleGroup, ExerciseTemplatesCallback callback) {
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
            List<ExerciseTemplate> filtered = exerciseTemplates.stream()
                    .filter(template -> template.getMuscleGroup().equalsIgnoreCase(muscleGroup))
                    .collect(Collectors.toList());
            callback.onSuccess(filtered);
        });
    }

    @Override
    public void searchExerciseTemplates(String query, ExerciseTemplatesCallback callback) {
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
            List<ExerciseTemplate> filtered = exerciseTemplates.stream()
                    .filter(template ->
                            template.getName().toLowerCase().contains(query.toLowerCase()) ||
                                    template.getMuscleGroup().toLowerCase().contains(query.toLowerCase()) ||
                                    template.getDescription().toLowerCase().contains(query.toLowerCase())
                    )
                    .collect(Collectors.toList());
            callback.onSuccess(filtered);
        });
    }
}