package com.erendogan6.planmyworkout.feature.workout.repository;

import com.erendogan6.planmyworkout.core.util.FirestoreManager;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLog;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.feature.workout.model.WorkoutPlan;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of the WorkoutRepository interface.
 * This class handles the communication with Firestore for workout operations.
 */
@Singleton
public class WorkoutRepositoryImpl implements WorkoutRepository {
    private final FirebaseFirestore firestore;
    private final FirestoreManager firestoreManager;

    @Inject
    public WorkoutRepositoryImpl(FirebaseFirestore firestore, FirestoreManager firestoreManager) {
        this.firestore = firestore;
        this.firestoreManager = firestoreManager;
    }

    /**
     * Get a workout plan by ID.
     *
     * @param planId The plan ID
     * @return Task with the workout plan
     */
    public Task<WorkoutPlan> getWorkoutPlan(String planId) {
        String userId = firestoreManager.getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not logged in"));
        }

        return firestore.collection("users")
                .document(userId)
                .collection("plans")
                .document(planId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Create a new WorkoutPlan object and populate it manually
                            WorkoutPlan plan = new WorkoutPlan();
                            plan.setId(document.getId());
                            plan.setName(document.getString("name"));
                            plan.setDescription(document.getString("description"));
                            plan.setDifficulty(document.getString("difficulty"));

                            // Get days per week and duration
                            Long daysPerWeek = document.getLong("daysPerWeek");
                            if (daysPerWeek != null) {
                                plan.setDaysPerWeek(daysPerWeek.intValue());
                            }

                            Long durationWeeks = document.getLong("durationWeeks");
                            if (durationWeeks != null) {
                                plan.setDurationWeeks(durationWeeks.intValue());
                            }

                            return plan;
                        }
                    }
                    return null;
                });
    }

    /**
     * Get an exercise by ID from a specific plan.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @return Task with the exercise with progress
     */
    public Task<ExerciseWithProgress> getExercise(String planId, String exerciseId) {
        String userId = firestoreManager.getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not logged in"));
        }

        return firestore.collection("users")
                .document(userId)
                .collection("plans")
                .document(planId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<Map<String, Object>> exercisesData = (List<Map<String, Object>>) document.get("exercises");
                            if (exercisesData != null) {
                                // Extract exercise ID index from format "ex_X"
                                int index = -1;
                                try {
                                    index = Integer.parseInt(exerciseId.substring(3));
                                } catch (Exception e) {
                                    // Invalid exerciseId format
                                    return null;
                                }

                                if (index >= 0 && index < exercisesData.size()) {
                                    Map<String, Object> exerciseData = exercisesData.get(index);
                                    String name = (String) exerciseData.get("name");
                                    String description = (String) exerciseData.get("description");
                                    String muscleGroup = (String) exerciseData.get("muscleGroup");
                                    Long setsLong = (Long) exerciseData.get("sets");
                                    Long repsLong = (Long) exerciseData.get("reps");
                                    Long restSecondsLong = (Long) exerciseData.get("restSeconds");

                                    int sets = setsLong != null ? setsLong.intValue() : 0;
                                    int reps = repsLong != null ? repsLong.intValue() : 0;
                                    int restSeconds = restSecondsLong != null ? restSecondsLong.intValue() : 60;

                                    return new ExerciseWithProgress(
                                            exerciseId,
                                            name != null ? name : "Unknown Exercise",
                                            description != null ? description : "",
                                            muscleGroup != null ? muscleGroup : "",
                                            "", // No image URL for now
                                            sets,
                                            reps,
                                            restSeconds,
                                            null, // No last weight yet
                                            null  // No last reps yet
                                    );
                                }
                            }
                        }
                    }
                    return null;
                });
    }

    /**
     * Get exercises for a workout plan.
     *
     * @param planId The plan ID
     * @return Task with the list of exercises with progress
     */
    public Task<List<ExerciseWithProgress>> getExercisesForPlan(String planId) {
        String userId = firestoreManager.getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not logged in"));
        }

        // First, get the plan to access the exercises array
        return firestore.collection("users")
                .document(userId)
                .collection("plans")
                .document(planId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<Map<String, Object>> exercisesData = (List<Map<String, Object>>) document.get("exercises");
                            if (exercisesData != null) {
                                List<ExerciseWithProgress> exercises = new ArrayList<>();

                                for (int i = 0; i < exercisesData.size(); i++) {
                                    Map<String, Object> exerciseData = exercisesData.get(i);
                                    String exerciseId = "ex_" + i; // Generate an ID based on position
                                    String name = (String) exerciseData.get("name");
                                    String description = (String) exerciseData.get("description");
                                    String muscleGroup = (String) exerciseData.get("muscleGroup");
                                    Long setsLong = (Long) exerciseData.get("sets");
                                    Long repsLong = (Long) exerciseData.get("reps");
                                    Long restSecondsLong = (Long) exerciseData.get("restSeconds");

                                    int sets = setsLong != null ? setsLong.intValue() : 0;
                                    int reps = repsLong != null ? repsLong.intValue() : 0;
                                    int restSeconds = restSecondsLong != null ? restSecondsLong.intValue() : 60;

                                    // Create the exercise with progress
                                    ExerciseWithProgress exercise = new ExerciseWithProgress(
                                            exerciseId,
                                            name != null ? name : "Unknown Exercise",
                                            description != null ? description : "",
                                            muscleGroup != null ? muscleGroup : "",
                                            "", // No image URL for now
                                            sets,
                                            reps,
                                            restSeconds,
                                            null, // No last weight yet
                                            null  // No last reps yet
                                    );

                                    exercises.add(exercise);
                                }

                                // Now, for each exercise, try to get the latest log to populate progress repository
                                return exercises;
                            }
                        }
                    }
                    return new ArrayList<ExerciseWithProgress>();
                })
                .continueWithTask(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<ExerciseWithProgress> exercises = task.getResult();
                        if (!exercises.isEmpty()) {
                            // Create a list of tasks to fetch the latest log for each exercise
                            List<Task<ExerciseWithProgress>> logTasks = new ArrayList<>();

                            for (ExerciseWithProgress exercise : exercises) {
                                Task<ExerciseWithProgress> logTask = getLatestExerciseLog(planId, exercise.getId())
                                        .continueWith(logResult -> {
                                            if (logResult.isSuccessful() && logResult.getResult() != null) {
                                                ExerciseLog log = logResult.getResult();
                                                exercise.setLastWeight(log.getWeight());
                                                exercise.setLastReps(log.getReps());
                                            }
                                            return exercise;
                                        });
                                logTasks.add(logTask);
                            }

                            // Wait for all log tasks to complete
                            return Tasks.whenAllSuccess(logTasks)
                                    .continueWith(allLogsTask -> exercises);
                        }
                    }
                    return Tasks.forResult(new ArrayList<ExerciseWithProgress>());
                });
    }

    /**
     * Get the latest exercise log for a specific exercise in a plan.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @return Task with the latest exercise log
     */
    public Task<ExerciseLog> getLatestExerciseLog(String planId, String exerciseId) {
        String userId = firestoreManager.getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not logged in"));
        }

        return firestore.collection("users")
                .document(userId)
                .collection("plans")
                .document(planId)
                .collection("exercises")
                .document(exerciseId)
                .collection("logs")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            String logId = document.getId();
                            Double weight = document.getDouble("weight");
                            Long reps = document.getLong("reps");
                            String notes = document.getString("notes");
                            Date timestamp = document.getDate("timestamp");

                            return new ExerciseLog(
                                    logId,
                                    weight != null ? weight : 0,
                                    reps != null ? reps.intValue() : 0,
                                    notes,
                                    timestamp
                            );
                        }
                    }
                    return null;
                });
    }

    /**
     * Get all exercise logs for a specific exercise in a plan.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @return Task with the list of exercise logs
     */
    @Override
    public Task<List<ExerciseLog>> getExerciseLogs(String planId, String exerciseId) {
        String userId = firestoreManager.getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not logged in"));
        }

        return firestore.collection("users")
                .document(userId)
                .collection("plans")
                .document(planId)
                .collection("exercises")
                .document(exerciseId)
                .collection("logs")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .continueWith(task -> {
                    List<ExerciseLog> logs = new ArrayList<>();
                    if (task.isSuccessful() && task.getResult() != null) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String logId = document.getId();
                            Double weight = document.getDouble("weight");
                            Long reps = document.getLong("reps");
                            String notes = document.getString("notes");
                            Date timestamp = document.getDate("timestamp");

                            logs.add(new ExerciseLog(
                                    logId,
                                    weight != null ? weight : 0,
                                    reps != null ? reps.intValue() : 0,
                                    notes,
                                    timestamp
                            ));
                        }
                    }
                    return logs;
                });
    }

    /**
     * Save a new exercise log for a specific exercise in a plan.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @param weight The weight used
     * @param reps The number of reps completed
     * @param notes Optional notes about the exercise
     * @return Task indicating success or failure
     */
    @Override
    public Task<Void> saveExerciseLog(String planId, String exerciseId, double weight, int reps, String notes) {
        String userId = firestoreManager.getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not logged in"));
        }

        // Create a new document with the current timestamp as the ID
        String timestamp = String.valueOf(new Date().getTime());
        DocumentReference logRef = firestore.collection("users")
                .document(userId)
                .collection("plans")
                .document(planId)
                .collection("exercises")
                .document(exerciseId)
                .collection("logs")
                .document(timestamp);

        // Create the log data
        Map<String, Object> logData = new HashMap<>();
        logData.put("weight", weight);
        logData.put("reps", reps);
        logData.put("notes", notes);
        logData.put("timestamp", new Date());

        // Save the log to Firestore
        return logRef.set(logData);
    }

    /**
     * Update an existing exercise log.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @param logId The log ID
     * @param weight The weight used
     * @param reps The number of reps completed
     * @param notes Optional notes about the exercise
     * @return Task indicating success or failure
     */
    @Override
    public Task<Void> updateExerciseLog(String planId, String exerciseId, String logId, double weight, int reps, String notes) {
        String userId = firestoreManager.getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not logged in"));
        }

        DocumentReference logRef = firestore.collection("users")
                .document(userId)
                .collection("plans")
                .document(planId)
                .collection("exercises")
                .document(exerciseId)
                .collection("logs")
                .document(logId);

        // Create the updated log data
        Map<String, Object> logData = new HashMap<>();
        logData.put("weight", weight);
        logData.put("reps", reps);
        logData.put("notes", notes);
        // Don't update the timestamp for edits

        // Update the log in Firestore
        return logRef.update(logData);
    }


}