package com.erendogan6.planmyworkout.feature.onboarding.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.erendogan6.planmyworkout.coreui.base.BaseFragment;

import com.erendogan6.planmyworkout.feature.onboarding.R;
import com.erendogan6.planmyworkout.feature.onboarding.adapter.ExerciseAdapter;
import com.erendogan6.planmyworkout.feature.onboarding.databinding.FragmentPlanDetailImprovedBinding;
import com.erendogan6.planmyworkout.feature.onboarding.model.Exercise;
import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.onboarding.viewmodel.PlanDetailViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for displaying workout plan details.
 */
@AndroidEntryPoint
public class PlanDetailFragment extends BaseFragment {

    private FragmentPlanDetailImprovedBinding binding;
    private PlanDetailViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlanDetailImprovedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(PlanDetailViewModel.class);

        // Get the plan ID from the arguments
        if (getArguments() != null) {
            PlanDetailFragmentArgs args = PlanDetailFragmentArgs.fromBundle(getArguments());
            String planId = args.getPlanId();
            viewModel.loadPlanDetails(planId);
        }

        // We don't need to set up the back button as it's handled by the MainActivity

        // Set up start plan button
        binding.btnStartPlan.setOnClickListener(v -> viewModel.savePlan());

        // Observe ViewModel
        observeViewModel();
    }

    private void observeViewModel() {
        // Observe workout plan
        viewModel.getWorkoutPlan().observe(getViewLifecycleOwner(), workoutPlan -> {
            if (workoutPlan != null) {
                updateUI(workoutPlan);
            }
        });

        // Observe exercises
        viewModel.getExercises().observe(getViewLifecycleOwner(), exercises -> {
            if (exercises != null && !exercises.isEmpty()) {
                setupExercisesRecyclerView(exercises);
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (Boolean.TRUE.equals(isLoading)) {
                showLoading();
                binding.btnStartPlan.setEnabled(false);
                binding.cardExercises.setAlpha(0.5f);
            } else {
                hideLoading();
                binding.btnStartPlan.setEnabled(true);
                binding.cardExercises.setAlpha(1.0f);
            }
        });

        // Observe save plan success
        viewModel.getSavePlanSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                // Plan saved successfully, navigate to home screen
                Toast.makeText(requireContext(), "Plan started! Redirecting to home screen.", Toast.LENGTH_SHORT).show();

                // Navigate to home using Navigation Component
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_planDetailFragment_to_bottom_nav_graph);
            }
        });

        // Observe error message
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUI(WorkoutPlan plan) {
        binding.tvPlanName.setText(plan.getName());
        binding.tvPlanDescription.setText(plan.getDescription());

        // Set plan stats
        binding.tvDifficulty.setText(plan.getDifficulty());
        binding.tvDaysPerWeek.setText(plan.getDaysPerWeek() + " days");
        binding.tvDuration.setText(plan.getDurationWeeks() + " weeks");

        // Format the schedule text based on the weekly schedule from Firestore
        StringBuilder scheduleBuilder = new StringBuilder();
        List<String> weeklySchedule = plan.getWeeklySchedule();

        if (weeklySchedule != null && !weeklySchedule.isEmpty()) {
            // Use the weekly schedule from Firestore
            for (int i = 0; i < weeklySchedule.size(); i++) {
                scheduleBuilder.append(weeklySchedule.get(i));
                if (i < weeklySchedule.size() - 1) {
                    scheduleBuilder.append("\n");
                }
            }
        }
        binding.tvSchedule.setText(scheduleBuilder.toString());
    }

    private void setupExercisesRecyclerView(List<Exercise> exercises) {
        ExerciseAdapter adapter = new ExerciseAdapter(exercises);
        binding.rvExercises.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvExercises.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
