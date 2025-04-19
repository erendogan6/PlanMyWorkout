package com.erendogan6.planmyworkout.presentation.home.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the home screen.
 * Manages the data for the home screen, including the current workout plan and motivational messages.
 */
@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final MutableLiveData<WorkoutPlan> currentWorkoutPlan = new MutableLiveData<>();
    private final MutableLiveData<String> motivationalMessage = new MutableLiveData<>();

    private final List<String> motivationalMessages = Arrays.asList(
            "Push yourself because no one else is going to do it for you.",
            "The only bad workout is the one that didn't happen.",
            "Your body can stand almost anything. It's your mind that you have to convince.",
            "The pain you feel today will be the strength you feel tomorrow.",
            "Don't stop when you're tired. Stop when you're done.",
            "No pain, no gain. Shut up and train.",
            "Your only limit is you.",
            "Sweat is just fat crying.",
            "The harder you work, the better you get.",
            "Make yourself proud."
    );

    @Inject
    public HomeViewModel() {
        // Set a random motivational message
        setRandomMotivationalMessage();

        // Set mock current workout plan (in a real app, this would come from a repository)
        setMockCurrentWorkoutPlan();
    }

    private void setRandomMotivationalMessage() {
        int randomIndex = new Random().nextInt(motivationalMessages.size());
        motivationalMessage.setValue(motivationalMessages.get(randomIndex));
    }

    private void setMockCurrentWorkoutPlan() {
        // Create a mock workout plan (in a real app, this would come from a repository)
        WorkoutPlan mockPlan = new WorkoutPlan(
                "1",
                "5x5 Strength Training",
                "A comprehensive strength training program focusing on compound movements with 5 sets of 5 reps.",
                "Intermediate",
                3,
                8
        );

        currentWorkoutPlan.setValue(mockPlan);
    }

    public LiveData<WorkoutPlan> getCurrentWorkoutPlan() {
        return currentWorkoutPlan;
    }

    public LiveData<String> getMotivationalMessage() {
        return motivationalMessage;
    }
}
