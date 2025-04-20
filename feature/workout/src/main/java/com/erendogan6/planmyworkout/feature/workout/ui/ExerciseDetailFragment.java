package com.erendogan6.planmyworkout.feature.workout.ui;

import android.os.Bundle;
import android.text.TextUtils;
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

import com.erendogan6.planmyworkout.feature.workout.databinding.FragmentExerciseDetailBinding;
import com.erendogan6.planmyworkout.feature.workout.viewmodel.ExerciseDetailViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for logging workout data for a specific exercise.
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

        // Get exercise ID from arguments
        if (getArguments() != null) {
            String exerciseId = ExerciseDetailFragmentArgs.fromBundle(getArguments()).getExerciseId();
            if (exerciseId != null && !exerciseId.isEmpty()) {
                // Load exercise details
                viewModel.loadExerciseDetails(exerciseId);
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
                
                // Set last try text
                if (exercise.hasLastTry()) {
                    binding.tvLastTry.setText(String.format("Last Try: %.1f kg × %d", 
                            exercise.getLastWeight(), exercise.getLastReps()));
                    
                    // Pre-fill the input fields with the last values
                    binding.etWeight.setText(String.format("%.1f", exercise.getLastWeight()));
                    binding.etReps.setText(String.valueOf(exercise.getLastReps()));
                } else {
                    binding.tvLastTry.setText("No previous attempts");
                }
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnSave.setEnabled(!isLoading);
        });

        // Observe save success
        viewModel.getSaveSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(requireContext(), "Exercise log saved successfully", Toast.LENGTH_SHORT).show();
                
                // Navigate back to the exercise list
                NavController navController = Navigation.findNavController(requireView());
                navController.popBackStack();
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

            // Save the exercise log
            viewModel.saveExerciseLog(weight, reps, notes);
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
