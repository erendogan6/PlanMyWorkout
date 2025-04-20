package com.erendogan6.planmyworkout.feature.onboarding.repository;

import com.erendogan6.planmyworkout.feature.onboarding.model.Exercise;
import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of the WorkoutPlanRepository interface.
 * This class handles the communication with Firestore.
 */
@Singleton
public class WorkoutPlanRepositoryImpl implements WorkoutPlanRepository {

    private final FirebaseFirestore firestore;
    private static final String COLLECTION_READY_WORKOUT_PLANS = "readyWorkoutPlans";

    @Inject
    public WorkoutPlanRepositoryImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void getReadyMadeWorkoutPlans(WorkoutPlansCallback callback) {
        firestore.collection(COLLECTION_READY_WORKOUT_PLANS)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<WorkoutPlan> workoutPlans = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            // Get basic plan data
                            String id = document.getId();
                            String name = document.getString("name");
                            String description = document.getString("description");
                            String difficulty = document.getString("difficulty");
                            Long daysLong = document.getLong("days");
                            int days = daysLong != null ? daysLong.intValue() : 0;

                            // Get duration weeks
                            Long durationWeeksLong = document.getLong("durationWeeks");
                            int durationWeeks = durationWeeksLong != null ? durationWeeksLong.intValue() : 4; // Default to 4 weeks

                            // Create workout plan
                            WorkoutPlan plan = new WorkoutPlan(id, name, description, difficulty, days, durationWeeks);

                            // Get weekly schedule
                            List<String> weeklySchedule = (List<String>) document.get("weeklySchedule");
                            if (weeklySchedule != null) {
                                plan.setWeeklySchedule(weeklySchedule);
                            }

                            // Get exercises
                            List<Map<String, Object>> exercisesData = (List<Map<String, Object>>) document.get("exercises");
                            if (exercisesData != null) {
                                List<Exercise> exercises = new ArrayList<>();

                                for (Map<String, Object> exerciseData : exercisesData) {
                                    String exerciseName = (String) exerciseData.get("name");
                                    Long setsLong = (Long) exerciseData.get("sets");
                                    Long repsLong = (Long) exerciseData.get("reps");
                                    String unit = (String) exerciseData.get("unit");
                                    String muscleGroup = (String) exerciseData.get("muscleGroup");
                                    String exerciseDescription = (String) exerciseData.get("description");
                                    Long restSecondsLong = (Long) exerciseData.get("restSeconds");

                                    int sets = setsLong != null ? setsLong.intValue() : 0;
                                    int reps = repsLong != null ? repsLong.intValue() : 0;
                                    int restSeconds = restSecondsLong != null ? restSecondsLong.intValue() : 60; // Default to 60 seconds

                                    // Create exercise with all available data
                                    Exercise exercise = new Exercise(
                                            "ex_" + exercises.size(), // Generate a simple ID
                                            exerciseName,
                                            exerciseDescription != null ? exerciseDescription : "", // Description from Firestore
                                            muscleGroup != null ? muscleGroup : "", // Muscle group from Firestore
                                            "", // No image URL
                                            sets,
                                            reps,
                                            restSeconds, // Rest seconds from Firestore
                                            unit != null ? unit : "reps" // Default to reps if not specified
                                    );

                                    exercises.add(exercise);
                                }

                                plan.setExercises(exercises);
                            }

                            workoutPlans.add(plan);
                        } catch (Exception e) {
                            // Skip this document if there's an error parsing it
                            e.printStackTrace();
                        }
                    }

                    callback.onSuccess(workoutPlans);
                })
                .addOnFailureListener(e -> {
                    callback.onError(e);
                });
    }
}
