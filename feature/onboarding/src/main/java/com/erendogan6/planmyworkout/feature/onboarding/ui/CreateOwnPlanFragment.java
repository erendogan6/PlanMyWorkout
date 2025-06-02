package com.erendogan6.planmyworkout.feature.onboarding.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.erendogan6.planmyworkout.feature.onboarding.R;
import com.erendogan6.planmyworkout.feature.onboarding.adapter.PlanExerciseAdapter;
import com.erendogan6.planmyworkout.feature.onboarding.databinding.FragmentCreateOwnPlanBinding;
import com.erendogan6.planmyworkout.feature.onboarding.dialog.ExerciseEditDialog;
import com.erendogan6.planmyworkout.feature.onboarding.dialog.ExerciseSelectionBottomSheet;
import com.erendogan6.planmyworkout.feature.onboarding.model.Exercise;
import com.erendogan6.planmyworkout.feature.onboarding.model.ExerciseTemplate;
import com.erendogan6.planmyworkout.feature.onboarding.viewmodel.CreateOwnPlanViewModel;
import com.google.android.material.chip.Chip;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for creating a custom workout plan.
 */
@AndroidEntryPoint
public class CreateOwnPlanFragment extends Fragment implements
        PlanExerciseAdapter.OnExerciseActionListener,
        ExerciseSelectionBottomSheet.OnExerciseSelectedListener,
        ExerciseEditDialog.OnExerciseEditListener {

    private FragmentCreateOwnPlanBinding binding;
    private CreateOwnPlanViewModel viewModel;
    private PlanExerciseAdapter exerciseAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateOwnPlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CreateOwnPlanViewModel.class);

        setupRecyclerView();
        setupListeners();
        observeViewModel();
    }

    private void setupRecyclerView() {
        exerciseAdapter = new PlanExerciseAdapter(this);
        binding.rvSelectedExercises.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSelectedExercises.setAdapter(exerciseAdapter);
    }

    private void setupListeners() {
        // Plan name input
        binding.etPlanName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.updatePlanName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Plan description input
        binding.etPlanDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.updatePlanDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Difficulty selection
        binding.chipGroupDifficulty.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip selectedChip = binding.getRoot().findViewById(checkedIds.get(0));
                if (selectedChip != null) {
                    viewModel.updateDifficulty(selectedChip.getText().toString());
                }
            }
        });

        // Days per week slider
        binding.sliderDays.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int days = (int) value;
                viewModel.updateDaysPerWeek(days);
                binding.tvDaysValue.setText(days + " days");
            }
        });

        // Add exercise button
        binding.btnAddExercise.setOnClickListener(v -> showExerciseSelectionBottomSheet());

        // Create plan button
        binding.btnCreatePlan.setOnClickListener(v -> {
            viewModel.savePlan();
        });
    }

    private void observeViewModel() {
        viewModel.getPlanState().observe(getViewLifecycleOwner(), planState -> {
            if (planState != null) {
                exerciseAdapter.updateExercises(planState.getExercises());
                updateExerciseVisibility(planState.getExercises().size());
                updatePlanSummary(planState.getExercises().size());
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getIsSaving().observe(getViewLifecycleOwner(), isSaving -> {
            binding.btnCreatePlan.setEnabled(!isSaving);
            binding.btnCreatePlan.setText(isSaving ? "Creating..." : "Create My Plan");
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                viewModel.clearErrorMessage();
            }
        });

        viewModel.getPlanSaved().observe(getViewLifecycleOwner(), planSaved -> {
            if (planSaved != null && planSaved) {
                Toast.makeText(requireContext(), "Plan created successfully!", Toast.LENGTH_SHORT).show();

                // Navigate to home using existing navigation
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_createOwnPlanFragment_to_bottom_nav_graph);
            }
        });
    }

    private void updateExerciseVisibility(int exerciseCount) {
        binding.layoutEmptyExercises.setVisibility(exerciseCount == 0 ? View.VISIBLE : View.GONE);
        binding.rvSelectedExercises.setVisibility(exerciseCount > 0 ? View.VISIBLE : View.GONE);
    }

    private void updatePlanSummary(int exerciseCount) {
        String summary = exerciseCount + " exercise" + (exerciseCount != 1 ? "s" : "") + " added";
        binding.tvPlanSummary.setText(summary);
    }

    private void showExerciseSelectionBottomSheet() {
        ExerciseSelectionBottomSheet bottomSheet = new ExerciseSelectionBottomSheet();
        bottomSheet.setOnExerciseSelectedListener(this);
        bottomSheet.show(getChildFragmentManager(), "ExerciseSelection");
    }

    // PlanExerciseAdapter.OnExerciseActionListener
    @Override
    public void onEditExercise(Exercise exercise, int position) {
        ExerciseEditDialog dialog = ExerciseEditDialog.newInstance(exercise, position);
        dialog.setOnExerciseEditListener(this);
        dialog.show(getChildFragmentManager(), "ExerciseEdit");
    }

    @Override
    public void onRemoveExercise(int position) {
        viewModel.removeExercise(position);
    }

    // ExerciseSelectionBottomSheet.OnExerciseSelectedListener
    @Override
    public void onExerciseSelected(ExerciseTemplate template) {
        viewModel.addExerciseFromTemplate(template);
    }

    // ExerciseEditDialog.OnExerciseEditListener
    @Override
    public void onExerciseUpdated(Exercise exercise, int position) {
        viewModel.updateExercise(position, exercise);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}