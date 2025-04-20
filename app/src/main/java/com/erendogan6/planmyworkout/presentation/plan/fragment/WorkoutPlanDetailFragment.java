package com.erendogan6.planmyworkout.presentation.plan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.databinding.FragmentWorkoutPlanDetailBinding;
import com.erendogan6.planmyworkout.domain.model.Exercise;
import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;
import com.erendogan6.planmyworkout.presentation.home.HomeActivity;
import com.erendogan6.planmyworkout.presentation.plan.adapter.ExerciseAdapter;
import com.erendogan6.planmyworkout.presentation.plan.viewmodel.WorkoutPlanDetailViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for displaying workout plan details.
 * This is a placeholder fragment that will be implemented in the next phase.
 */
@AndroidEntryPoint
public class WorkoutPlanDetailFragment extends Fragment {

    public static final String ARG_WORKOUT_PLAN = "arg_workout_plan";

    private FragmentWorkoutPlanDetailBinding binding;
    private WorkoutPlanDetailViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutPlanDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(WorkoutPlanDetailViewModel.class);

        // Get workout plan from arguments
        if (getArguments() != null && getArguments().containsKey(ARG_WORKOUT_PLAN)) {
            WorkoutPlan workoutPlan = (WorkoutPlan) getArguments().getSerializable(ARG_WORKOUT_PLAN);
            viewModel.setWorkoutPlan(workoutPlan);
        }

        // Set up back button
        binding.toolbar.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.popBackStack();
        });

        // Set up start plan button
        binding.btnStartPlan.setOnClickListener(v -> viewModel.savePlan());

        // Observe ViewModel
        observeViewModel();
    }

    private void observeViewModel() {
        // Observe workout plan
        viewModel.getWorkoutPlan().observe(getViewLifecycleOwner(), workoutPlan -> {
            if (workoutPlan != null) {
                // Set up toolbar title
                binding.tvToolbarTitle.setText(workoutPlan.getName());

                // Set plan details
                binding.tvPlanName.setText(workoutPlan.getName());

                // Format the details text
                String details = "Difficulty: " + workoutPlan.getDifficulty() +
                        " • " + workoutPlan.getDaysPerWeek() + " days/week" +
                        " • " + workoutPlan.getDurationWeeks() + " weeks";
                binding.tvPlanDetails.setText(details);

                // Set plan description
                binding.tvPlanDescription.setText(workoutPlan.getDescription());
            }
        });

        // Observe exercises
        viewModel.getExercises().observe(getViewLifecycleOwner(), exercises -> {
            if (exercises != null && !exercises.isEmpty()) {
                // Set up RecyclerView
                setupExercisesRecyclerView(exercises);
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnStartPlan.setEnabled(!isLoading);
            binding.scrollView.setAlpha(isLoading ? 0.5f : 1.0f);
        });

        // Observe save plan success
        viewModel.getSavePlanSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                // Plan saved successfully, navigate to home screen
                Toast.makeText(requireContext(), "Plan saved successfully!", Toast.LENGTH_SHORT).show();

                // Navigate to the home screen using Navigation Component
                Intent intent = new Intent(requireContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
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
