package com.erendogan6.planmyworkout.feature.onboarding.repository;

import com.erendogan6.planmyworkout.feature.onboarding.model.Exercise;
import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
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
    private static final String COLLECTION_USERS = "users";
    private static final String SUBCOLLECTION_PLANS = "plans";



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


    @Override
    public void saveUserWorkoutPlan(String userId, WorkoutPlan plan, SavePlanCallback callback) {
        if (userId == null || plan == null) {
            callback.onError(new IllegalArgumentException("User ID and plan must not be null"));
            return;
        }

        // Create a map to store the plan data
        Map<String, Object> planData = new HashMap<>();
        planData.put("id", plan.getId());
        planData.put("name", plan.getName());
        planData.put("description", plan.getDescription());
        planData.put("difficulty", plan.getDifficulty());
        planData.put("daysPerWeek", plan.getDaysPerWeek());
        planData.put("durationWeeks", plan.getDurationWeeks());

        // Add weekly schedule if available
        if (plan.getWeeklySchedule() != null) {
            planData.put("weeklySchedule", plan.getWeeklySchedule());
        }

        // Add exercises if available
        if (plan.getExercises() != null) {
            List<Map<String, Object>> exercisesData = new ArrayList<>();
            for (Exercise exercise : plan.getExercises()) {
                Map<String, Object> exerciseData = new HashMap<>();
                exerciseData.put("name", exercise.getName());
                exerciseData.put("description", exercise.getDescription());
                exerciseData.put("muscleGroup", exercise.getMuscleGroup());
                exerciseData.put("sets", exercise.getSets());
                exerciseData.put("reps", exercise.getRepsPerSet());
                exerciseData.put("restSeconds", exercise.getRestSeconds());
                exerciseData.put("unit", exercise.getUnit());
                exercisesData.add(exerciseData);
            }
            planData.put("exercises", exercisesData);
        }

        // Save the plan to Firestore in the user's plans subcollection
        firestore.collection(COLLECTION_USERS)
                .document(userId)
                .collection(SUBCOLLECTION_PLANS)
                .document(plan.getId())
                .set(planData)
                .addOnSuccessListener(aVoid -> {
                    // Also save the plan ID as the main plan directly in the user document
                    // Use set with merge option instead of update to create the document if it doesn't exist
                    Map<String, Object> mainPlanData = new HashMap<>();
                    mainPlanData.put("mainPlanId", plan.getId());

                    firestore.collection(COLLECTION_USERS)
                            .document(userId)
                            .set(mainPlanData, SetOptions.merge())  // Using set with merge instead of update
                            .addOnSuccessListener(aVoid1 -> {
                                callback.onSuccess();
                            })
                            .addOnFailureListener(e -> {
                                callback.onError(e);
                            });
                })
                .addOnFailureListener(e -> {
                    callback.onError(e);
                });
    }
}
