package com.erendogan6.planmyworkout.feature.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.erendogan6.planmyworkout.coreui.base.BaseFragment;

import com.erendogan6.planmyworkout.feature.home.R;
import com.erendogan6.planmyworkout.feature.home.databinding.FragmentHomeBinding;
import com.erendogan6.planmyworkout.feature.home.viewmodel.HomeViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Home screen fragment.
 */
@AndroidEntryPoint
public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setupListeners();
        observeViewModel();
    }

    private void setupListeners() {
        // Set up current plan card click listener
        binding.cardCurrentPlan.setOnClickListener(v -> {
            // Navigate to workout screen with the current plan ID
            if (viewModel.getCurrentPlanId() != null) {
                // Create a bundle with the plan ID
                Bundle args = new Bundle();
                args.putString("planId", viewModel.getCurrentPlanId());

                // Navigate to the workout navigation graph
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_homeFragment_to_workout_navigation, args);
            }
        });

        // Set up create workout plan section
        binding.layoutCreateHeader.setOnClickListener(v -> {
            // Toggle visibility of create options
            boolean isVisible = binding.layoutCreateOptions.getVisibility() == View.VISIBLE;
            binding.layoutCreateOptions.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            // Rotate arrow based on expanded state
            binding.ivExpandArrow.setRotation(isVisible ? 0 : 180);
        });

        // Set up create options buttons
        binding.btnReadyMadePlan.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_homeFragment_to_readyMadePlansFragment));

        binding.btnBuildOwnPlan.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_homeFragment_to_createPlanFragment));

        binding.btnGenerateAI.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_homeFragment_to_aiGeneratePlanFragment));
    }

    private void observeViewModel() {
        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (Boolean.TRUE.equals(isLoading)) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        viewModel.getUserName().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                binding.tvMotivationalMessage.setText("Keep pushing, " + name + "! You're doing great!");
            }
        });

        viewModel.getCurrentPlan().observe(getViewLifecycleOwner(), plan -> {
            if (plan != null) {
                // Set plan name
                binding.tvCurrentPlanName.setText(plan.getName());

                // Set schedule
                if (plan.getWeeklySchedule() != null && !plan.getWeeklySchedule().isEmpty()) {
                    binding.tvPlanSchedule.setText(String.join(", ", plan.getWeeklySchedule()));
                } else {
                    binding.tvPlanSchedule.setText(plan.getDaysPerWeek() + " days per week");
                }

                // Set duration
                binding.tvPlanDuration.setText(plan.getDurationWeeks() + " weeks program");

                // Set exercises preview
                if (plan.getExerciseNames() != null && !plan.getExerciseNames().isEmpty()) {
                    // Show first 3 exercises with ellipsis if there are more
                    List<String> previewExercises = plan.getExerciseNames().subList(
                            0, Math.min(3, plan.getExerciseNames().size()));
                    String exercisesText = String.join(", ", previewExercises);
                    if (plan.getExerciseNames().size() > 3) {
                        exercisesText += "...";
                    }
                    binding.tvPlanExercises.setText(exercisesText);
                } else {
                    binding.tvPlanExercises.setVisibility(View.GONE);
                }

                // Show the plan layout and hide the no plan message
                binding.tvNoWorkoutPlan.setVisibility(View.GONE);
                binding.layoutCurrentPlan.setVisibility(View.VISIBLE);
            } else {
                // No plan available, show the no plan message
                binding.tvNoWorkoutPlan.setVisibility(View.VISIBLE);
                binding.layoutCurrentPlan.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
