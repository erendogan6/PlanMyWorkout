package com.erendogan6.planmyworkout.presentation.home.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.databinding.FragmentHomeBinding;
import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;
import com.erendogan6.planmyworkout.presentation.home.viewmodel.HomeViewModel;
import com.erendogan6.planmyworkout.presentation.plan.fragment.WorkoutPlanDetailFragment;
import com.erendogan6.planmyworkout.presentation.plan.fragment.ReadyMadePlansFragment;

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
                navigateToWorkoutPlanDetail(currentPlan);
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

    private void navigateToWorkoutPlanDetail(WorkoutPlan workoutPlan) {
        // For now, just show a toast message since this feature is not ready yet
        Toast.makeText(requireContext(), "Viewing current workout details coming soon!", Toast.LENGTH_SHORT).show();

        // In the future, this would navigate to a workout detail or tracking screen
        // The code below is commented out until that screen is ready

        /*
        // Create bundle with workout plan
        Bundle args = new Bundle();
        args.putSerializable(WorkoutPlanDetailFragment.ARG_WORKOUT_PLAN, workoutPlan);

        // Create and navigate to detail fragment
        WorkoutPlanDetailFragment fragment = new WorkoutPlanDetailFragment();
        fragment.setArguments(args);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
        */
    }

    private void navigateToReadyMadePlans() {
        // Navigate to ready-made plans screen
        ReadyMadePlansFragment fragment = new ReadyMadePlansFragment();

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void navigateToBuildOwnPlan() {
        // TODO: Navigate to build own plan screen
        // This would be implemented when the build own plan feature is ready
    }

    private void navigateToGenerateAI() {
        // TODO: Navigate to AI generation screen
        // This would be implemented when the AI generation feature is ready
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
