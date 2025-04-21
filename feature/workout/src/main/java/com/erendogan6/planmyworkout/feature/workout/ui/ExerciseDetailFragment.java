package com.erendogan6.planmyworkout.feature.workout.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.erendogan6.planmyworkout.feature.workout.databinding.FragmentExerciseDetailBinding;
import com.erendogan6.planmyworkout.feature.workout.viewmodel.ExerciseDetailViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for logging workout repository for a specific exercise.
 */
@AndroidEntryPoint
public class ExerciseDetailFragment extends Fragment {

    private FragmentExerciseDetailBinding binding;
    private ExerciseDetailViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExerciseDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ExerciseDetailViewModel.class);

        // Set up back button
        binding.toolbar.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.popBackStack();
        });

        // Get arguments
        if (getArguments() != null) {
            ExerciseDetailFragmentArgs args = ExerciseDetailFragmentArgs.fromBundle(getArguments());
            String exerciseId = args.getExerciseId();
            String planId = args.getPlanId();
            boolean isEdit = args.getIsEdit();
            String logId = args.getLogId();
            float weight = args.getWeight();
            int reps = args.getReps();
            String notes = args.getNotes();

            if (!exerciseId.isEmpty() && !planId.isEmpty()) {
                // Load exercise details
                viewModel.loadExerciseDetails();

                // If in edit mode, pre-fill the form with the log data
                if (isEdit && !logId.isEmpty()) {
                    viewModel.setEditMode(true, logId);
                    binding.tvTitle.setText("Edit Log");
                    binding.etWeight.setText(String.format(Locale.getDefault(), "%.1f", weight));
                    binding.etReps.setText(String.valueOf(reps));
                    binding.etNotes.setText(notes);
                    binding.layoutLastTry.setVisibility(View.GONE);
                } else {
                    // In create mode, load the latest log for reference
                    viewModel.loadLatestLog();
                    binding.tvTitle.setText("Add New Log");
                }
            }
        }

        // Set up save button
        binding.btnSave.setOnClickListener(v -> saveExerciseLog());

        // Observe ViewModel
        observeViewModel();
    }

    private void observeViewModel() {
        // Observe exercise
        viewModel.getExercise().observe(getViewLifecycleOwner(), exercise -> {
            if (exercise != null) {
                binding.tvExerciseName.setText(exercise.getName());
            }
        });

        // Observe latest log
        viewModel.getLatestLog().observe(getViewLifecycleOwner(), log -> {
            if (log != null) {
                // Set last try text with formatted date
                binding.tvLastTry.setText(String.format("Last Try: %.1f kg × %d on %s",
                        log.getWeight(), log.getReps(), log.getFormattedDate()));

                // Show notes if available
                if (log.getNotes() != null && !log.getNotes().isEmpty()) {
                    binding.tvLastNotes.setVisibility(View.VISIBLE);
                    binding.tvLastNotes.setText(log.getNotes());
                } else {
                    binding.tvLastNotes.setVisibility(View.GONE);
                }

                // Pre-fill the input fields with the last values
                binding.etWeight.setText(String.format("%.1f", log.getWeight()));
                binding.etReps.setText(String.valueOf(log.getReps()));

                // Show the last try section
                binding.layoutLastTry.setVisibility(View.VISIBLE);
            } else {
                // No previous log
                binding.tvLastTry.setText("No previous attempts");
                binding.tvLastNotes.setVisibility(View.GONE);
                binding.layoutLastTry.setVisibility(View.VISIBLE);
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        // Observe saving state
        viewModel.getIsSaving().observe(getViewLifecycleOwner(), isSaving -> {
            binding.progressBarSave.setVisibility(Boolean.TRUE.equals(isSaving) ? View.VISIBLE : View.GONE);
            binding.btnSave.setEnabled(!isSaving);
            binding.etWeight.setEnabled(!isSaving);
            binding.etReps.setEnabled(!isSaving);
            binding.etNotes.setEnabled(!isSaving);
        });

        // Observe save success
        viewModel.getSaveSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(requireContext(), "Exercise log saved successfully", Toast.LENGTH_SHORT).show();

                // Clear input fields
                binding.etWeight.setText("");
                binding.etReps.setText("");
                binding.etNotes.setText("");
            }
        });

        // Observe error message
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveExerciseLog() {
        // Validate input
        String weightStr = binding.etWeight.getText().toString().trim();
        String repsStr = binding.etReps.getText().toString().trim();
        String notes = binding.etNotes.getText().toString().trim();

        if (TextUtils.isEmpty(weightStr)) {
            binding.etWeight.setError("Please enter weight");
            return;
        }

        if (TextUtils.isEmpty(repsStr)) {
            binding.etReps.setError("Please enter reps");
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            int reps = Integer.parseInt(repsStr);

            // Save or update the exercise log based on mode
            if (viewModel.isInEditMode()) {
                viewModel.updateExerciseLog(weight, reps, notes);
            } else {
                viewModel.saveExerciseLog(weight, reps, notes);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
