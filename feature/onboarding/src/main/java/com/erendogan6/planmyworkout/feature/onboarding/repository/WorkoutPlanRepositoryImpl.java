package com.erendogan6.planmyworkout.feature.onboarding.repository;

import android.util.Log;

import com.erendogan6.planmyworkout.feature.onboarding.model.Exercise;
import com.erendogan6.planmyworkout.feature.onboarding.model.ExerciseDocument;
import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlanDocument;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of the WorkoutPlanRepository interface.
 * Handles all Firestore operations related to workout plans.
 */
@Singleton
public class WorkoutPlanRepositoryImpl implements WorkoutPlanRepository {

    private static final String TAG = "WorkoutPlanRepo";
    private static final String COLLECTION_READY_WORKOUT_PLANS = "readyWorkoutPlans";
    private static final String COLLECTION_USERS = "users";
    private static final String SUBCOLLECTION_PLANS = "plans";
    private static final String FIELD_MAIN_PLAN_ID = "mainPlanId";

    private static final int DEFAULT_DAYS = 0;
    private static final int DEFAULT_DURATION_WEEKS = 4;
    private static final int DEFAULT_REST_SECONDS = 60;
    private static final String DEFAULT_UNIT = "reps";

    private final FirebaseFirestore firestore;

    @Inject
    public WorkoutPlanRepositoryImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    /**
     * Retrieves all ready-made workout plans from Firestore.
     *
     * @param callback Callback for handling the result
     */
    @Override
    public void getReadyMadeWorkoutPlans(WorkoutPlansCallback callback) {
        firestore.collection(COLLECTION_READY_WORKOUT_PLANS)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->
                        callback.onSuccess(processDocuments(queryDocumentSnapshots)))
                .addOnFailureListener(callback::onError);
    }

    /**
     * Processes a QuerySnapshot and converts it to a list of WorkoutPlan objects.
     *
     * @param queryDocumentSnapshots The QuerySnapshot to process
     * @return List of WorkoutPlan objects
     */
    private List<WorkoutPlan> processDocuments(QuerySnapshot queryDocumentSnapshots) {
        return queryDocumentSnapshots.getDocuments().stream()
                .map(document -> convertDocumentToWorkoutPlan((QueryDocumentSnapshot) document))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Converts a Firestore document to a WorkoutPlan object.
     *
     * @param document The Firestore document to convert
     * @return WorkoutPlan object or null if conversion fails
     */
    private WorkoutPlan convertDocumentToWorkoutPlan(QueryDocumentSnapshot document) {
        try {
            WorkoutPlanDocument planDocument = document.toObject(WorkoutPlanDocument.class);
            return buildWorkoutPlan(document.getId(), planDocument);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing document: " + document.getId(), e);
            return null;
        }
    }

    /**
     * Builds a WorkoutPlan object from a WorkoutPlanDocument.
     *
     * @param id The document ID
     * @param planDocument The plan document repository
     * @return A fully constructed WorkoutPlan object
     */
    private WorkoutPlan buildWorkoutPlan(String id, WorkoutPlanDocument planDocument) {
        WorkoutPlan plan = createBasicWorkoutPlan(id, planDocument);

        if (planDocument.getWeeklySchedule() != null) {
            plan.setWeeklySchedule(planDocument.getWeeklySchedule());
        }

        if (planDocument.getExercises() != null) {
            plan.setExercises(convertExercises(planDocument.getExercises()));
        }

        return plan;
    }

    /**
     * Creates a basic WorkoutPlan object with essential properties.
     *
     * @param id The plan ID
     * @param planDocument The plan document repository
     * @return A basic WorkoutPlan object
     */
    private WorkoutPlan createBasicWorkoutPlan(String id, WorkoutPlanDocument planDocument) {
        return new WorkoutPlan(
                id,
                planDocument.getName(),
                planDocument.getDescription(),
                planDocument.getDifficulty(),
                getIntValue(planDocument.getDays(), DEFAULT_DAYS),
                getIntValue(planDocument.getDurationWeeks(), DEFAULT_DURATION_WEEKS)
        );
    }

    /**
     * Converts a list of ExerciseDocument objects to Exercise objects.
     *
     * @param exerciseDocs List of ExerciseDocument objects
     * @return List of Exercise objects
     */
    private List<Exercise> convertExercises(List<ExerciseDocument> exerciseDocs) {
        List<Exercise> exercises = new ArrayList<>(exerciseDocs.size());

        for (int i = 0; i < exerciseDocs.size(); i++) {
            exercises.add(convertExerciseDocument(exerciseDocs.get(i), i));
        }

        return exercises;
    }

    /**
     * Converts an ExerciseDocument to an Exercise object.
     *
     * @param exerciseDoc The exercise document to convert
     * @param index The index for generating exercise ID
     * @return Exercise object
     */
    private Exercise convertExerciseDocument(ExerciseDocument exerciseDoc, int index) {
        return new Exercise(
                generateExerciseId(index),
                exerciseDoc.getName(),
                getOrDefault(exerciseDoc.getDescription(), ""),
                getOrDefault(exerciseDoc.getMuscleGroup(), ""),
                "",
                getIntValue(exerciseDoc.getSets(), 0),
                getIntValue(exerciseDoc.getReps(), 0),
                getIntValue(exerciseDoc.getRestSeconds(), DEFAULT_REST_SECONDS),
                getOrDefault(exerciseDoc.getUnit(), DEFAULT_UNIT)
        );
    }

    /**
     * Generates a unique exercise ID based on index.
     *
     * @param index The index of the exercise
     * @return A unique exercise ID
     */
    private String generateExerciseId(int index) {
        return "ex_" + index;
    }

    /**
     * Safely converts a Long value to int with a default value.
     *
     * @param value The Long value to convert
     * @param defaultValue The default value if the input is null
     * @return The converted int value
     */
    private int getIntValue(Long value, int defaultValue) {
        return value != null ? value.intValue() : defaultValue;
    }

    /**
     * Returns the original string or a default value if the input is null.
     *
     * @param value The input string
     * @param defaultValue The default value to return if input is null
     * @return The original string or default value
     */
    private String getOrDefault(String value, String defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * Saves a workout plan for a specific user in Firestore.
     *
     * @param userId   The ID of the user
     * @param plan     The WorkoutPlan to save
     * @param callback Callback to handle success or failure of the operation
     */
    @Override
    public void saveUserWorkoutPlan(String userId, WorkoutPlan plan, SavePlanCallback callback) {
        if (!validateInputs(userId, plan, callback)) {
            return;
        }

        WorkoutPlanDocument planDocument = WorkoutPlanDocument.fromWorkoutPlan(plan);
        savePlanAndUpdateMainPlanId(userId, plan.getId(), planDocument, callback);
    }

    /**
     * Validates the input parameters for saving a workout plan.
     *
     * @param userId   The user ID to validate
     * @param plan     The plan to validate
     * @param callback The callback to notify if validation fails
     * @return true if inputs are valid, false otherwise
     */
    private boolean validateInputs(String userId, WorkoutPlan plan, SavePlanCallback callback) {
        if (userId == null || plan == null) {
            callback.onError(new IllegalArgumentException("User ID and plan must not be null"));
            return false;
        }
        return true;
    }

    /**
     * Saves a workout plan and updates the user's main plan ID.
     *
     * @param userId       The ID of the user
     * @param planId       The ID of the plan
     * @param planDocument The document to save
     * @param callback     Callback to handle the operation result
     */
    private void savePlanAndUpdateMainPlanId(String userId, String planId,
                                             WorkoutPlanDocument planDocument,
                                             SavePlanCallback callback) {
        firestore.collection(COLLECTION_USERS)
                .document(userId)
                .collection(SUBCOLLECTION_PLANS)
                .document(planId)
                .set(planDocument)
                .addOnSuccessListener(aVoid -> updateMainPlanId(userId, planId, callback))
                .addOnFailureListener(callback::onError);
    }

    /**
     * Updates the main plan ID in the user's document.
     *
     * @param userId   The ID of the user
     * @param planId   The ID of the plan to set as main
     * @param callback Callback to handle the operation result
     */
    private void updateMainPlanId(String userId, String planId, SavePlanCallback callback) {
        Map<String, Object> mainPlanData = new HashMap<>();
        mainPlanData.put(FIELD_MAIN_PLAN_ID, planId);

        firestore.collection(COLLECTION_USERS)
                .document(userId)
                .set(mainPlanData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(callback::onError);
    }
}