package com.erendogan6.planmyworkout.feature.onboarding.usecase;

import com.erendogan6.planmyworkout.feature.onboarding.model.ExerciseTemplate;
import com.erendogan6.planmyworkout.feature.onboarding.repository.ExerciseTemplateRepository;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.util.List;

import javax.inject.Inject;

public class GetExerciseTemplatesUseCase {

    private final ExerciseTemplateRepository repository;

    @Inject
    public GetExerciseTemplatesUseCase(ExerciseTemplateRepository repository) {
        this.repository = repository;
    }

    public Task<List<ExerciseTemplate>> execute() {
        TaskCompletionSource<List<ExerciseTemplate>> taskSource = new TaskCompletionSource<>();

        repository.getExerciseTemplates(new ExerciseTemplateRepository.ExerciseTemplatesCallback() {
            @Override
            public void onSuccess(List<ExerciseTemplate> templates) {
                taskSource.setResult(templates);
            }

            @Override
            public void onError(Exception e) {
                taskSource.setException(e);
            }
        });

        return taskSource.getTask();
    }

    public Task<List<ExerciseTemplate>> executeByMuscleGroup(String muscleGroup) {
        TaskCompletionSource<List<ExerciseTemplate>> taskSource = new TaskCompletionSource<>();

        repository.getExerciseTemplatesByMuscleGroup(muscleGroup, new ExerciseTemplateRepository.ExerciseTemplatesCallback() {
            @Override
            public void onSuccess(List<ExerciseTemplate> templates) {
                taskSource.setResult(templates);
            }

            @Override
            public void onError(Exception e) {
                taskSource.setException(e);
            }
        });

        return taskSource.getTask();
    }

    public Task<List<ExerciseTemplate>> executeSearch(String query) {
        TaskCompletionSource<List<ExerciseTemplate>> taskSource = new TaskCompletionSource<>();

        repository.searchExerciseTemplates(query, new ExerciseTemplateRepository.ExerciseTemplatesCallback() {
            @Override
            public void onSuccess(List<ExerciseTemplate> templates) {
                taskSource.setResult(templates);
            }

            @Override
            public void onError(Exception e) {
                taskSource.setException(e);
            }
        });

        return taskSource.getTask();
    }
}