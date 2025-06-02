package com.erendogan6.planmyworkout.feature.onboarding.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.erendogan6.planmyworkout.coreui.base.BaseFragment;
import com.erendogan6.planmyworkout.feature.onboarding.R;
import com.erendogan6.planmyworkout.feature.onboarding.databinding.FragmentAiGeneratePlanBinding;
import com.erendogan6.planmyworkout.feature.onboarding.model.AiWorkoutPlanResponse;
import com.erendogan6.planmyworkout.feature.onboarding.viewmodel.AiGeneratePlanViewModel;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for generating a workout plan using AI.
 */
@AndroidEntryPoint
public class AiGeneratePlanFragment extends BaseFragment {
    private FragmentAiGeneratePlanBinding binding;
    private AiGeneratePlanViewModel viewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAiGeneratePlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AiGeneratePlanViewModel.class);

        setupListeners();
        observeViewModel();
        setupSliders();
    }

    private void setupListeners() {
        binding.btnGeneratePlan.setOnClickListener(v -> generatePlan());
    }

    private void setupSliders() {
        binding.sliderDays.addOnChangeListener((slider, value, fromUser) -> {
            binding.tvDaysValue.setText((int) value + " days");
        });

        binding.sliderDuration.addOnChangeListener((slider, value, fromUser) -> {
            binding.tvDurationValue.setText((int) value + " min");
        });
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading){
                showLoading();
            } else {
                hideLoading();
            }
            binding.btnGeneratePlan.setEnabled(!isLoading);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getGeneratedPlan().observe(getViewLifecycleOwner(), plan -> {
            if (plan != null && plan.getExercises() != null) {
                String message = String.format("✅ '%s' created with %d exercises!",
                        plan.getName(), plan.getExercises().size());
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

                // Navigate to preview screen
                navigateToPlanPreview(plan);
            }
        });
    }

    private void navigateToPlanPreview(AiWorkoutPlanResponse plan) {
        try {
            // Plan'ı Bundle ile gönder
            Bundle bundle = new Bundle();
            bundle.putString("plan_name", plan.getName());
            bundle.putString("plan_description", plan.getDescription());
            bundle.putString("plan_difficulty", plan.getDifficulty());
            bundle.putInt("plan_days", plan.getDays());
            bundle.putInt("plan_duration_weeks", plan.getDurationWeeks());

            // Exercises'ı JSON olarak serialize et
            Gson gson = new Gson();
            bundle.putString("exercises_json", gson.toJson(plan.getExercises()));

            Navigation.findNavController(requireView()).navigate(
                    R.id.action_aiGeneratePlanFragment_to_aiPlanPreviewFragment,
                    bundle
            );

        } catch (Exception e) {
            Log.e("AiGenerateFragment", "Navigation error", e);
            Toast.makeText(requireContext(), "Error showing plan preview", Toast.LENGTH_SHORT).show();
        }
    }

    private void generatePlan() {
        String goal = binding.etGoal.getText().toString().trim();
        if (goal.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter your fitness goal", Toast.LENGTH_SHORT).show();
            return;
        }

        String experience = getSelectedExperience();
        if (experience.isEmpty()) {
            Toast.makeText(requireContext(), "Please select your experience level", Toast.LENGTH_SHORT).show();
            return;
        }

        int daysPerWeek = (int) binding.sliderDays.getValue();
        int sessionDuration = (int) binding.sliderDuration.getValue();
        String equipment = binding.etEquipment.getText().toString().trim();
        String limitations = binding.etLimitations.getText().toString().trim();
        String focusAreas = binding.etFocusAreas.getText().toString().trim();

        // Set defaults if empty
        if (equipment.isEmpty()) equipment = "Basic gym equipment";
        if (limitations.isEmpty()) limitations = "No physical limitations";
        if (focusAreas.isEmpty()) focusAreas = "Full body strength and fitness";

        viewModel.generateWorkoutPlan(goal, experience, daysPerWeek, sessionDuration,
                equipment, limitations, focusAreas);
    }

    private String getSelectedExperience() {
        int checkedId = binding.chipGroupExperience.getCheckedChipId();
        if (checkedId == View.NO_ID) return "";

        Chip selectedChip = binding.chipGroupExperience.findViewById(checkedId);
        return selectedChip != null ? selectedChip.getText().toString() : "";
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}