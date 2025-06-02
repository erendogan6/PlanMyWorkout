package com.erendogan6.planmyworkout.feature.onboarding.repository;

import com.erendogan6.planmyworkout.feature.onboarding.model.ExerciseTemplate;
import java.util.List;

public interface ExerciseTemplateRepository {

    interface ExerciseTemplatesCallback {
        void onSuccess(List<ExerciseTemplate> templates);
        void onError(Exception e);
    }

    void getExerciseTemplates(ExerciseTemplatesCallback callback);
    void getExerciseTemplatesByMuscleGroup(String muscleGroup, ExerciseTemplatesCallback callback);
    void searchExerciseTemplates(String query, ExerciseTemplatesCallback callback);
}