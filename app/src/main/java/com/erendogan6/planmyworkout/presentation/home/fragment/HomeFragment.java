package com.erendogan6.planmyworkout.presentation.home.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.databinding.FragmentHomeBinding;
import com.erendogan6.planmyworkout.domain.model.Exercise;
import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;
import com.erendogan6.planmyworkout.presentation.home.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for the home screen.
 * Displays the user's current workout plan and options to create new plans.
 */
@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private boolean isCreateSectionExpanded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Set up UI components
        setupMotivationalMessage();
        setupCurrentWorkoutPlan();
        setupCreateWorkoutSection();

        // Observe ViewModel
        observeViewModel();
    }

    private void setupMotivationalMessage() {
        // Set a random motivational message
        viewModel.getMotivationalMessage().observe(getViewLifecycleOwner(), message -> {
            binding.tvMotivationalMessage.setText(message);
        });
    }

    private void setupCurrentWorkoutPlan() {
        // Set up click listener for current workout plan
        binding.cardCurrentPlan.setOnClickListener(v -> {
            WorkoutPlan currentPlan = viewModel.getCurrentWorkoutPlan().getValue();
            if (currentPlan != null) {
                navigateToExerciseList(currentPlan);
            }
        });
    }

    private void setupCreateWorkoutSection() {
        // Set up the collapsible section
        binding.layoutCreateHeader.setOnClickListener(v -> toggleCreateSection());

        // Set up click listeners for create options
        binding.btnReadyMadePlan.setOnClickListener(v -> navigateToReadyMadePlans());
        binding.btnBuildOwnPlan.setOnClickListener(v -> navigateToBuildOwnPlan());
        binding.btnGenerateAI.setOnClickListener(v -> navigateToGenerateAI());

        // Initially collapse the create section
        updateCreateSectionState(false);
    }

    private void toggleCreateSection() {
        updateCreateSectionState(!isCreateSectionExpanded);
    }

    private void updateCreateSectionState(boolean expanded) {
        isCreateSectionExpanded = expanded;

        // Rotate the arrow icon
        float rotation = expanded ? 180f : 0f;
        ObjectAnimator.ofFloat(binding.ivExpandArrow, "rotation", rotation).start();

        // Show/hide the options
        binding.layoutCreateOptions.setVisibility(expanded ? View.VISIBLE : View.GONE);
    }

    private void observeViewModel() {
        // Observe current workout plan
        viewModel.getCurrentWorkoutPlan().observe(getViewLifecycleOwner(), workoutPlan -> {
            if (workoutPlan != null) {
                binding.tvCurrentPlanName.setText(workoutPlan.getName());
                binding.layoutCurrentPlan.setVisibility(View.VISIBLE);
                binding.tvNoWorkoutPlan.setVisibility(View.GONE);
            } else {
                binding.layoutCurrentPlan.setVisibility(View.GONE);
                binding.tvNoWorkoutPlan.setVisibility(View.VISIBLE);
            }
        });
    }

    private void navigateToExerciseList(WorkoutPlan workoutPlan) {
        // Get mock exercises for the workout plan
        List<Exercise> exercises = getMockExercisesForPlan(workoutPlan);

        // Create the navigation action with Safe Args
        HomeFragmentDirections.ActionHomeFragmentToExerciseListFragment action =
                HomeFragmentDirections.actionHomeFragmentToExerciseListFragment(
                        workoutPlan,
                        new ArrayList<>(exercises));

        // Get NavController
        NavController navController = Navigation.findNavController(requireView());

        // Navigate using the action
        navController.navigate(action);
    }

    /**
     * Get mock exercises for a workout plan.
     * In a real app, this would come from a repository.
     */
    private List<Exercise> getMockExercisesForPlan(WorkoutPlan plan) {
        List<Exercise> exerciseList = new ArrayList<>();

        // Add mock exercises based on the plan type
        if (plan.getName().contains("Full Body")) {
            // Full body workout exercises
            exerciseList.add(new Exercise("ex1", "Barbell Squat", "A compound lower body exercise that targets the quadriceps, hamstrings, and glutes", "Legs", "", 3, 10, 90));
            exerciseList.add(new Exercise("ex2", "Bench Press", "A compound upper body exercise that targets the chest, shoulders, and triceps", "Chest", "", 3, 10, 90));
            exerciseList.add(new Exercise("ex3", "Deadlift", "A compound full body exercise that targets the back, glutes, and hamstrings", "Back", "", 3, 8, 120));
            exerciseList.add(new Exercise("ex4", "Pull-ups", "A compound upper body exercise that targets the back and biceps", "Back", "", 3, 8, 90));
            exerciseList.add(new Exercise("ex5", "Overhead Press", "A compound upper body exercise that targets the shoulders and triceps", "Shoulders", "", 3, 10, 90));
            exerciseList.add(new Exercise("ex6", "Dumbbell Lunges", "A unilateral lower body exercise that targets the quadriceps, hamstrings, and glutes", "Legs", "", 3, 12, 60));
        } else if (plan.getName().contains("Push Pull")) {
            // Push day exercises
            exerciseList.add(new Exercise("ex7", "Incline Bench Press", "A compound upper body exercise that targets the upper chest, shoulders, and triceps", "Chest", "", 4, 8, 90));
            exerciseList.add(new Exercise("ex8", "Dumbbell Shoulder Press", "A compound upper body exercise that targets the shoulders and triceps", "Shoulders", "", 4, 10, 90));
            exerciseList.add(new Exercise("ex9", "Tricep Pushdowns", "An isolation exercise that targets the triceps", "Arms", "", 3, 12, 60));
            exerciseList.add(new Exercise("ex10", "Lateral Raises", "An isolation exercise that targets the lateral deltoids", "Shoulders", "", 3, 15, 60));
            exerciseList.add(new Exercise("ex11", "Chest Flyes", "An isolation exercise that targets the chest", "Chest", "", 3, 12, 60));
        } else if (plan.getName().contains("Upper Lower")) {
            // Upper body exercises
            exerciseList.add(new Exercise("ex12", "Barbell Rows", "A compound upper body exercise that targets the back and biceps", "Back", "", 4, 8, 90));
            exerciseList.add(new Exercise("ex13", "Incline Dumbbell Press", "A compound upper body exercise that targets the upper chest, shoulders, and triceps", "Chest", "", 4, 10, 90));
            exerciseList.add(new Exercise("ex14", "Pull-ups", "A compound upper body exercise that targets the back and biceps", "Back", "", 3, 8, 90));
            exerciseList.add(new Exercise("ex15", "Dips", "A compound upper body exercise that targets the chest, shoulders, and triceps", "Chest", "", 3, 10, 90));
            exerciseList.add(new Exercise("ex16", "Face Pulls", "A compound upper body exercise that targets the rear deltoids and upper back", "Shoulders", "", 3, 15, 60));
        } else {
            // Default exercises for any other plan
            exerciseList.add(new Exercise("ex17", "Push-ups", "A bodyweight exercise that targets the chest, shoulders, and triceps", "Chest", "", 3, 15, 60));
            exerciseList.add(new Exercise("ex18", "Bodyweight Squats", "A bodyweight exercise that targets the quadriceps, hamstrings, and glutes", "Legs", "", 3, 20, 60));
            exerciseList.add(new Exercise("ex19", "Plank", "A core exercise that targets the abdominals and lower back", "Core", "", 3, 30, 60));
            exerciseList.add(new Exercise("ex20", "Mountain Climbers", "A cardio exercise that targets the core, shoulders, and legs", "Full Body", "", 3, 20, 30));
            exerciseList.add(new Exercise("ex21", "Burpees", "A full body exercise that targets multiple muscle groups and provides cardiovascular benefits", "Full Body", "", 3, 15, 60));
        }

        return exerciseList;
    }

    private void navigateToReadyMadePlans() {
        // Navigate to ready-made plans screen using Navigation Component
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_homeFragment_to_readyMadePlansFragment);
    }

    private void navigateToBuildOwnPlan() {
        // Navigate to build own plan screen using Navigation Component
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_homeFragment_to_createPlanFragment);
    }

    private void navigateToGenerateAI() {
        // Navigate to AI generation screen using Navigation Component
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_homeFragment_to_aiPlanGenerationFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
