package com.erendogan6.planmyworkout.feature.onboarding.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.erendogan6.planmyworkout.feature.onboarding.adapter.ExerciseTemplateAdapter;
import com.erendogan6.planmyworkout.feature.onboarding.databinding.BottomSheetExerciseSelectionBinding;
import com.erendogan6.planmyworkout.feature.onboarding.model.ExerciseTemplate;
import com.erendogan6.planmyworkout.feature.onboarding.viewmodel.CreateOwnPlanViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ExerciseSelectionBottomSheet extends BottomSheetDialogFragment implements
        ExerciseTemplateAdapter.OnExerciseTemplateClickListener {

    public interface OnExerciseSelectedListener {
        void onExerciseSelected(ExerciseTemplate template);
    }

    private BottomSheetExerciseSelectionBinding binding;
    private CreateOwnPlanViewModel viewModel;
    private ExerciseTemplateAdapter templateAdapter;
    private OnExerciseSelectedListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
            View bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetExerciseSelectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireParentFragment()).get(CreateOwnPlanViewModel.class);

        setupRecyclerView();
        setupListeners();
        observeViewModel();
    }

    private void setupRecyclerView() {
        templateAdapter = new ExerciseTemplateAdapter(this);
        binding.rvExerciseTemplates.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvExerciseTemplates.setAdapter(templateAdapter);
    }

    private void setupListeners() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    viewModel.filterExercisesByMuscleGroup(getSelectedMuscleGroup());
                } else {
                    viewModel.searchExercises(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.chipGroupMuscleGroups.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip selectedChip = binding.getRoot().findViewById(checkedIds.get(0));
                if (selectedChip != null) {
                    String muscleGroup = selectedChip.getText().toString();
                    viewModel.filterExercisesByMuscleGroup(muscleGroup);
                }
            }
        });

        binding.btnClose.setOnClickListener(v -> dismiss());
    }

    private void observeViewModel() {
        viewModel.getFilteredTemplates().observe(this, templates -> {
            if (templates != null) {
                templateAdapter.updateTemplates(templates);
                binding.tvNoResults.setVisibility(templates.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    private String getSelectedMuscleGroup() {
        int checkedId = binding.chipGroupMuscleGroups.getCheckedChipId();
        if (checkedId != View.NO_ID) {
            Chip selectedChip = binding.getRoot().findViewById(checkedId);
            return selectedChip != null ? selectedChip.getText().toString() : "All";
        }
        return "All";
    }

    @Override
    public void onExerciseTemplateClick(ExerciseTemplate template) {
        if (listener != null) {
            listener.onExerciseSelected(template);
        }
        dismiss();
    }

    public void setOnExerciseSelectedListener(OnExerciseSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}