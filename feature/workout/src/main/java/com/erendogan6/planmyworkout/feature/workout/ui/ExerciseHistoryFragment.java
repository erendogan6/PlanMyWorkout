package com.erendogan6.planmyworkout.feature.workout.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.erendogan6.planmyworkout.coreui.base.BaseFragment;

import com.erendogan6.planmyworkout.feature.workout.adapter.ExerciseLogAdapter;
import com.erendogan6.planmyworkout.feature.workout.databinding.FragmentExerciseHistoryBinding;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLog;
import com.erendogan6.planmyworkout.feature.workout.viewmodel.ExerciseHistoryViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for displaying the history of logs for a specific exercise.
 */
@AndroidEntryPoint
public class ExerciseHistoryFragment extends BaseFragment implements ExerciseLogAdapter.OnLogSelectedListener {

    private FragmentExerciseHistoryBinding binding;
    private ExerciseHistoryViewModel viewModel;
    private ExerciseLogAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExerciseHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ExerciseHistoryViewModel.class);

        // Set up back button
        binding.toolbar.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.popBackStack();
        });

        // Set up RecyclerView
        setupRecyclerView();

        // Set up FAB
        binding.fabAddLog.setOnClickListener(v -> navigateToExerciseDetail(false, null));

        // Load exercise data
        viewModel.loadExerciseData();

        // Observe ViewModel
        observeViewModel();
    }

    private void setupRecyclerView() {
        binding.rvLogs.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ExerciseLogAdapter(new ArrayList<>(), this);
        binding.rvLogs.setAdapter(adapter);
    }

    private void observeViewModel() {
        // Observe exercise
        viewModel.getExercise().observe(getViewLifecycleOwner(), exercise -> {
            if (exercise != null) {
                binding.tvExerciseName.setText(exercise.getName());

                // Set muscle group if available
                if (exercise.getMuscleGroup() != null && !exercise.getMuscleGroup().isEmpty()) {
                    binding.tvMuscleGroup.setText(exercise.getMuscleGroup());
                    binding.tvMuscleGroup.setVisibility(View.VISIBLE);
                } else {
                    binding.tvMuscleGroup.setVisibility(View.GONE);
                }
            }
        });

        // Observe logs
        viewModel.getLogs().observe(getViewLifecycleOwner(), logs -> {
            if (logs != null) {
                adapter.updateLogs(logs);
                updateEmptyState(logs.isEmpty());
            } else {
                updateEmptyState(true);
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoading();
                binding.rvLogs.setVisibility(View.GONE);
            } else {
                hideLoading();
                binding.rvLogs.setVisibility(View.VISIBLE);
            }
        });

        // Observe error message
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateEmptyState(boolean isEmpty) {
        binding.layoutEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.rvLogs.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onLogSelected(ExerciseLog log) {
        // Navigate to exercise detail for editing
        navigateToExerciseDetail(true, log);
    }

    private void navigateToExerciseDetail(boolean isEdit, ExerciseLog log) {
        String exerciseId = viewModel.getExerciseId();
        String planId = viewModel.getPlanId();

        if (exerciseId == null || planId == null) {
            Toast.makeText(requireContext(), "Exercise ID or Plan ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        ExerciseHistoryFragmentDirections.ActionExerciseHistoryFragmentToExerciseDetailFragment action =
                ExerciseHistoryFragmentDirections.actionExerciseHistoryFragmentToExerciseDetailFragment(
                        exerciseId, planId);

        // Set edit mode and log ID if editing
        if (isEdit && log != null) {
            action.setIsEdit(true);
            action.setLogId(log.getId());
            action.setWeight((float) log.getWeight());
            action.setReps(log.getReps());
            action.setNotes(log.getNotes() != null ? log.getNotes() : "");
        }

        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh logs when returning to this fragment
        viewModel.refreshLogs();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
