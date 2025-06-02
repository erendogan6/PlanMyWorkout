package com.erendogan6.planmyworkout.feature.onboarding.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erendogan6.planmyworkout.feature.onboarding.R;
import com.erendogan6.planmyworkout.feature.onboarding.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class PlanExerciseAdapter extends RecyclerView.Adapter<PlanExerciseAdapter.PlanExerciseViewHolder> {

    private List<Exercise> exercises;
    private final OnExerciseActionListener listener;

    public interface OnExerciseActionListener {
        void onEditExercise(Exercise exercise, int position);
        void onRemoveExercise(int position);
    }

    public PlanExerciseAdapter(OnExerciseActionListener listener) {
        this.exercises = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlanExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan_exercise, parent, false);
        return new PlanExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.bind(exercise, position, listener);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void updateExercises(List<Exercise> newExercises) {
        this.exercises = newExercises != null ? newExercises : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class PlanExerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvExerciseNumber;
        private final TextView tvExerciseName;
        private final TextView tvMuscleGroup;
        private final TextView tvSetsReps;
        private final TextView tvRest;
        private final ImageButton btnEdit;
        private final ImageButton btnRemove;

        public PlanExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseNumber = itemView.findViewById(R.id.tvExerciseNumber);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvMuscleGroup = itemView.findViewById(R.id.tvMuscleGroup);
            tvSetsReps = itemView.findViewById(R.id.tvSetsReps);
            tvRest = itemView.findViewById(R.id.tvRest);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }

        public void bind(Exercise exercise, int position, OnExerciseActionListener listener) {
            tvExerciseNumber.setText(String.valueOf(position + 1));
            tvExerciseName.setText(exercise.getName());
            tvMuscleGroup.setText(exercise.getMuscleGroup());
            tvSetsReps.setText(String.format("%d × %d", exercise.getSets(), exercise.getReps()));
            tvRest.setText(String.format("%ds rest", exercise.getRestSeconds()));

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditExercise(exercise, position);
                }
            });

            btnRemove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveExercise(position);
                }
            });
        }
    }
}