package com.erendogan6.planmyworkout.feature.onboarding.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.erendogan6.planmyworkout.feature.onboarding.databinding.DialogExerciseEditBinding;
import com.erendogan6.planmyworkout.feature.onboarding.model.Exercise;

public class ExerciseEditDialog extends DialogFragment {

    public interface OnExerciseEditListener {
        void onExerciseUpdated(Exercise exercise, int position);
    }

    private static final String ARG_EXERCISE = "exercise";
    private static final String ARG_POSITION = "position";

    private DialogExerciseEditBinding binding;
    private Exercise exercise;
    private int position;
    private OnExerciseEditListener listener;

    public static ExerciseEditDialog newInstance(Exercise exercise, int position) {
        ExerciseEditDialog dialog = new ExerciseEditDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXERCISE, exercise);
        args.putInt(ARG_POSITION, position);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exercise = (Exercise) getArguments().getSerializable(ARG_EXERCISE);
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogExerciseEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews();
        setupListeners();
    }

    private void setupViews() {
        if (exercise != null) {
            binding.tvExerciseName.setText(exercise.getName());
            binding.tvMuscleGroup.setText(exercise.getMuscleGroup());
            binding.etSets.setText(String.valueOf(exercise.getSets()));
            binding.etReps.setText(String.valueOf(exercise.getReps()));
            binding.etRest.setText(String.valueOf(exercise.getRestSeconds()));
        }
    }

    private void setupListeners() {
        binding.btnCancel.setOnClickListener(v -> dismiss());

        binding.btnSave.setOnClickListener(v -> {
            if (validateAndSave()) {
                dismiss();
            }
        });
    }

    private boolean validateAndSave() {
        try {
            int sets = Integer.parseInt(binding.etSets.getText().toString().trim());
            int reps = Integer.parseInt(binding.etReps.getText().toString().trim());
            int rest = Integer.parseInt(binding.etRest.getText().toString().trim());

            if (sets <= 0 || reps <= 0 || rest < 0) {
                binding.tvError.setText("Please enter valid positive numbers");
                binding.tvError.setVisibility(View.VISIBLE);
                return false;
            }

            Exercise updatedExercise = new Exercise(
                    exercise.getId(),
                    exercise.getName(),
                    exercise.getDescription(),
                    exercise.getMuscleGroup(),
                    exercise.getImageUrl(),
                    sets,
                    reps,
                    rest,
                    exercise.getUnit()
            );

            if (listener != null) {
                listener.onExerciseUpdated(updatedExercise, position);
            }

            return true;

        } catch (NumberFormatException e) {
            binding.tvError.setText("Please enter valid numbers");
            binding.tvError.setVisibility(View.VISIBLE);
            return false;
        }
    }

    public void setOnExerciseEditListener(OnExerciseEditListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}