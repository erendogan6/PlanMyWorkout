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
import com.erendogan6.planmyworkout.feature.onboarding.adapter.AiExercisePreviewAdapter;
import com.erendogan6.planmyworkout.feature.onboarding.databinding.FragmentAiPlanPreviewBinding;
import com.erendogan6.planmyworkout.feature.onboarding.model.AiWorkoutPlanResponse;
import com.erendogan6.planmyworkout.feature.onboarding.viewmodel.AiGeneratePlanViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AiPlanPreviewFragment extends BaseFragment {

    private FragmentAiPlanPreviewBinding binding;
    private AiWorkoutPlanResponse currentPlan;
    private AiExercisePreviewAdapter adapter;
    private AiGeneratePlanViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAiPlanPreviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AiGeneratePlanViewModel.class);

        loadPlanFromArguments();
        setupUI();
        setupListeners();
        observeViewModel();
    }

    private void loadPlanFromArguments() {
        Bundle args = getArguments();
        if (args != null) {
            currentPlan = new AiWorkoutPlanResponse();
            currentPlan.setName(args.getString("plan_name", "AI Generated Plan"));
            currentPlan.setDescription(args.getString("plan_description", "Personalized workout plan"));
            currentPlan.setDifficulty(args.getString("plan_difficulty", "Intermediate"));
            currentPlan.setDays(args.getInt("plan_days", 3));
            currentPlan.setDurationWeeks(args.getInt("plan_duration_weeks", 4));

            // Deserialize exercises
            String exercisesJson = args.getString("exercises_json", "[]");
            Gson gson = new Gson();
            Type listType = new TypeToken<List<AiWorkoutPlanResponse.AiExercise>>(){}.getType();
            List<AiWorkoutPlanResponse.AiExercise> exercises = gson.fromJson(exercisesJson, listType);
            currentPlan.setExercises(exercises);
        }
    }

    private void setupUI() {
        if (currentPlan != null) {
            binding.tvPlanName.setText(currentPlan.getName());
            binding.tvPlanDescription.setText(currentPlan.getDescription());
            binding.tvDifficulty.setText(currentPlan.getDifficulty());
            binding.tvDays.setText(currentPlan.getDays() + " days/week");

            // Setup RecyclerView
            binding.rvExercises.setLayoutManager(new LinearLayoutManager(requireContext()));
            adapter = new AiExercisePreviewAdapter(currentPlan.getExercises());
            binding.rvExercises.setAdapter(adapter);
        }
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).popBackStack();
        });

        binding.btnRegenerate.setOnClickListener(v -> regeneratePlan());

        binding.btnReject.setOnClickListener(v -> regeneratePlan());

        binding.btnAccept.setOnClickListener(v -> acceptPlan());
    }

    private void observeViewModel() {
        viewModel.getIsSaving().observe(getViewLifecycleOwner(), isSaving -> {
            binding.btnAccept.setEnabled(!isSaving);
            binding.btnAccept.setText(isSaving ? "Saving..." : "Use This Plan");
        });

        viewModel.getPlanSaved().observe(getViewLifecycleOwner(), planSaved -> {
            if (planSaved != null && planSaved) {
                Toast.makeText(requireContext(), "Plan saved successfully! 🎉", Toast.LENGTH_SHORT).show();

                // Navigate to main app
                Navigation.findNavController(requireView()).navigate(
                        com.erendogan6.planmyworkout.feature.onboarding.R.id.action_aiPlanPreviewFragment_to_bottom_nav_graph
                );
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void regeneratePlan() {
        Navigation.findNavController(requireView()).navigate(
                com.erendogan6.planmyworkout.feature.onboarding.R.id.action_aiPlanPreviewFragment_to_aiGeneratePlanFragment
        );
    }

    private void acceptPlan() {
        if (currentPlan != null) {
            viewModel.saveAiWorkoutPlan(currentPlan);
        } else {
            Toast.makeText(requireContext(), "No plan to save", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}