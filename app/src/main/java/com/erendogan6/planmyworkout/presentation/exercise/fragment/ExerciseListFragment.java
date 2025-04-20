package com.erendogan6.planmyworkout.presentation.exercise.fragment;

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

import com.erendogan6.planmyworkout.databinding.FragmentExerciseListBinding;
import com.erendogan6.planmyworkout.domain.model.Exercise;
import com.erendogan6.planmyworkout.domain.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;
import com.erendogan6.planmyworkout.presentation.exercise.adapter.ExerciseListAdapter;
import com.erendogan6.planmyworkout.presentation.exercise.viewmodel.ExerciseListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for displaying a list of exercises in a workout plan.
 */
@AndroidEntryPoint
public class ExerciseListFragment extends Fragment implements ExerciseListAdapter.OnExerciseSelectedListener {

    // Constants for argument keys (for backward compatibility)
    public static final String ARG_WORKOUT_PLAN = "workoutPlan";
    public static final String ARG_EXERCISES = "exercisesList";

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

        // Get workout plan and exercises from arguments using Safe Args
        ExerciseListFragmentArgs args = ExerciseListFragmentArgs.fromBundle(getArguments());

        // Get workout plan
        WorkoutPlan workoutPlan = args.getWorkoutPlan();
        if (workoutPlan != null) {
            binding.tvPlanTitle.setText(workoutPlan.getName());
            viewModel.setWorkoutPlan(workoutPlan);
        }

        // Get exercises
        ArrayList<Exercise> exercises = args.getExercisesList();
        if (exercises != null && !exercises.isEmpty()) {
            // Convert exercises to exercises with progress
           List<ExerciseWithProgress> exercisesWithProgress = createMockExercisesWithProgress(exercises);
            viewModel.setExercises(exercisesWithProgress);
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
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.rvExercises.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });
    }

    private void updateEmptyState(boolean isEmpty) {
        binding.tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.rvExercises.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    /**
     * Create mock exercises with progress data.
     * In a real app, this would come from a repository.
     */
    private List<ExerciseWithProgress> createMockExercisesWithProgress(List<Exercise> exercises) {
        List<ExerciseWithProgress> result = new ArrayList<>();
        Random random = new Random();

        for (Exercise exercise : exercises) {
            // Randomly decide if this exercise has previous attempts
            boolean hasLastTry = random.nextBoolean();

            if (hasLastTry) {
                // Generate random weight (between 20 and 150 kg)
                double weight = 20 + random.nextInt(131);
                // Generate random reps (between 1 and 12)
                int reps = 1 + random.nextInt(12);

                result.add(ExerciseWithProgress.fromExerciseWithProgress(exercise, weight, reps));
            } else {
                result.add(ExerciseWithProgress.fromExercise(exercise));
            }
        }

        return result;
    }

    @Override
    public void onExerciseSelected(ExerciseWithProgress exercise) {
        // Navigate to exercise detail screen
        Toast.makeText(requireContext(), "Selected: " + exercise.getName(), Toast.LENGTH_SHORT).show();

        // In a real app, we would navigate to the exercise detail screen
        // NavController navController = Navigation.findNavController(requireView());
        // Bundle args = new Bundle();
        // args.putSerializable("exercise", exercise);
        // navController.navigate(R.id.action_exerciseListFragment_to_exerciseDetailFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
