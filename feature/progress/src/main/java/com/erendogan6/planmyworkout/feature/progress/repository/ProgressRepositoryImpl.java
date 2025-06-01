package com.erendogan6.planmyworkout.feature.progress.repository;

import com.erendogan6.planmyworkout.feature.progress.model.ExerciseDetail;
import com.erendogan6.planmyworkout.feature.progress.model.SetDetail;
import com.erendogan6.planmyworkout.feature.progress.model.WorkoutSummary;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of ProgressRepository using Firebase Firestore.
 */
@Singleton
public class ProgressRepositoryImpl implements ProgressRepository {

    private final FirebaseFirestore firestore;
    private final FirebaseAuth firebaseAuth;

    @Inject
    public ProgressRepositoryImpl(FirebaseFirestore firestore, FirebaseAuth firebaseAuth) {
        this.firestore = firestore;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public Task<WorkoutSummary> getWorkoutSummaryForDate(LocalDate date) {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not authenticated"));
        }

        Date startOfDay = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfDay = Date.from(date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Query the flat exerciseLogs collection (no collection group needed)
        return firestore.collection("users")
                .document(userId)
                .collection("exerciseLogs")
                .whereGreaterThanOrEqualTo("timestamp", startOfDay)
                .whereLessThan("timestamp", endOfDay)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        QuerySnapshot querySnapshot = task.getResult();

                        if (querySnapshot.isEmpty()) {
                            return null;
                        }

                        return calculateWorkoutSummary(querySnapshot);
                    } else {
                        throw new RuntimeException("Failed to get workout summary", task.getException());
                    }
                });
    }

    @Override
    public Task<List<ExerciseDetail>> getExerciseDetailsForDate(LocalDate date) {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not authenticated"));
        }

        Date startOfDay = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfDay = Date.from(date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Query the flat exerciseLogs collection
        return firestore.collection("users")
                .document(userId)
                .collection("exerciseLogs")
                .whereGreaterThanOrEqualTo("timestamp", startOfDay)
                .whereLessThan("timestamp", endOfDay)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        QuerySnapshot querySnapshot = task.getResult();
                        return processExerciseDetails(querySnapshot);
                    } else {
                        throw new RuntimeException("Failed to get exercise details", task.getException());
                    }
                });
    }

    private List<ExerciseDetail> processExerciseDetails(QuerySnapshot querySnapshot) {
        Map<String, List<SetDetail>> exerciseMap = new HashMap<>();

        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            String exerciseName = document.getString("exerciseName");
            Long reps = document.getLong("reps");
            Double weight = document.getDouble("weight");
            String notes = document.getString("notes");

            if (exerciseName != null && reps != null && weight != null) {
                SetDetail setDetail = new SetDetail(reps.intValue(), weight, notes);

                exerciseMap.computeIfAbsent(exerciseName, k -> new ArrayList<>()).add(setDetail);
            }
        }

        List<ExerciseDetail> exerciseDetails = new ArrayList<>();
        for (Map.Entry<String, List<SetDetail>> entry : exerciseMap.entrySet()) {
            exerciseDetails.add(new ExerciseDetail(entry.getKey(), entry.getValue()));
        }

        return exerciseDetails;
    }

    @Override
    public Task<List<WorkoutSummary>> getWorkoutDataForDateRange(LocalDate startDate, LocalDate endDate) {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not authenticated"));
        }

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        return firestore.collectionGroup("exerciseLogs")
                .whereEqualTo("userId", userId)
                .whereGreaterThanOrEqualTo("timestamp", start)
                .whereLessThan("timestamp", end)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        QuerySnapshot querySnapshot = task.getResult();
                        return processWorkoutDataRange(querySnapshot, startDate, endDate);
                    } else {
                        throw new RuntimeException("Failed to get workout data range", task.getException());
                    }
                });
    }

    private WorkoutSummary calculateWorkoutSummary(QuerySnapshot querySnapshot) {
        int totalSets = 0;
        int totalReps = 0;
        Map<String, Boolean> uniqueExercises = new HashMap<>();
        long sessionTimeMinutes = 36; // Default session time - can be calculated from timestamps

        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            String exerciseName = document.getString("exerciseName");
            Long reps = document.getLong("reps");

            totalSets++;
            if (reps != null) {
                totalReps += reps.intValue();
            }
            if (exerciseName != null) {
                uniqueExercises.put(exerciseName, true);
            }
        }

        return new WorkoutSummary(totalSets, totalReps, uniqueExercises.size(), sessionTimeMinutes);
    }
    private List<WorkoutSummary> processWorkoutDataRange(QuerySnapshot querySnapshot, LocalDate startDate, LocalDate endDate) {
        // Group by date and calculate summaries for each day
        Map<LocalDate, List<DocumentSnapshot>> dateGroups = new HashMap<>();

        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            Date timestamp = document.getDate("timestamp");
            if (timestamp != null) {
                LocalDate date = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                dateGroups.computeIfAbsent(date, k -> new ArrayList<>()).add(document);
            }
        }

        List<WorkoutSummary> summaries = new ArrayList<>();
        for (Map.Entry<LocalDate, List<DocumentSnapshot>> entry : dateGroups.entrySet()) {
            // Create a QuerySnapshot-like object for each day and calculate summary
            // This is simplified - in a real implementation you might want to refactor this
            summaries.add(calculateWorkoutSummaryFromDocuments(entry.getValue()));
        }

        return summaries;
    }

    private WorkoutSummary calculateWorkoutSummaryFromDocuments(List<DocumentSnapshot> documents) {
        int totalSets = documents.size();
        int totalReps = 0;
        Map<String, Boolean> uniqueExercises = new HashMap<>();

        for (DocumentSnapshot document : documents) {
            String exerciseName = document.getString("exerciseName");
            Long reps = document.getLong("reps");

            if (reps != null) {
                totalReps += reps.intValue();
            }
            if (exerciseName != null) {
                uniqueExercises.put(exerciseName, true);
            }
        }

        return new WorkoutSummary(totalSets, totalReps, uniqueExercises.size(), 36);
    }

    private String getCurrentUserId() {
        return firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;
    }
}