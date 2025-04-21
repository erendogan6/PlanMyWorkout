package com.erendogan6.planmyworkout.feature.workout.ui;

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

import com.erendogan6.planmyworkout.feature.workout.adapter.ExerciseListAdapter;
import com.erendogan6.planmyworkout.feature.workout.databinding.FragmentExerciseListBinding;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.feature.workout.viewmodel.ExerciseListViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for displaying a list of exercises in a workout plan.
 */
@AndroidEntryPoint
public class ExerciseListFragment extends Fragment implements ExerciseListAdapter.OnExerciseSelectedListener {

    private FragmentExerciseListBinding binding;
    private ExerciseListViewModel viewModel;
    private ExerciseListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExerciseListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ExerciseListViewModel.class);

        // Set up back button
        binding.toolbar.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.popBackStack();
        });

        // Get plan ID from arguments
        if (getArguments() != null) {
            String planId = ExerciseListFragmentArgs.fromBundle(getArguments()).getPlanId();
            if (!planId.isEmpty()) {
                // Load exercises for the plan
                viewModel.loadExercisesForPlan(planId);
            }
        }

        // Set up RecyclerView
        setupRecyclerView();

        // Observe ViewModel
        observeViewModel();
    }

    private void setupRecyclerView() {
        binding.rvExercises.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ExerciseListAdapter(new ArrayList<>(), this);
        binding.rvExercises.setAdapter(adapter);
    }

    private void observeViewModel() {
        // Observe workout plan
        viewModel.getWorkoutPlan().observe(getViewLifecycleOwner(), plan -> {
            if (plan != null) {
                binding.tvPlanTitle.setText(plan.getName());
            }
        });

        // Observe exercises
        viewModel.getExercises().observe(getViewLifecycleOwner(), exercises -> {
            if (exercises != null) {
                adapter.updateExercises(exercises);
                updateEmptyState(exercises.isEmpty());
            } else {
                updateEmptyState(true);
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(Boolean.TRUE.equals(isLoading) ? View.VISIBLE : View.GONE);
            binding.rvExercises.setVisibility(Boolean.TRUE.equals(isLoading) ? View.GONE : View.VISIBLE);
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateEmptyState(boolean isEmpty) {
        binding.tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.rvExercises.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onExerciseSelected(ExerciseWithProgress exercise) {
        // Get the plan ID from arguments
        String planId = null;
        if (getArguments() != null) {
            planId = ExerciseListFragmentArgs.fromBundle(getArguments()).getPlanId();
        }

        if (planId == null || planId.isEmpty()) {
            Toast.makeText(requireContext(), "Plan ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Navigate to exercise history screen
        Toast.makeText(requireContext(), "Selected: " + exercise.getName(), Toast.LENGTH_SHORT).show();

        // Navigate to exercise history with both exerciseId and planId
        ExerciseListFragmentDirections.ActionExerciseListFragmentToExerciseHistoryFragment action =
                ExerciseListFragmentDirections.actionExerciseListFragmentToExerciseHistoryFragment(exercise.getId(), planId);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
