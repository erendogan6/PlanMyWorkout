package com.erendogan6.planmyworkout.feature.workout.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erendogan6.planmyworkout.feature.workout.databinding.ItemExerciseListBinding;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseWithProgress;

import java.util.List;

/**
 * Adapter for displaying exercises with progress in a RecyclerView.
 */
public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {

    private List<ExerciseWithProgress> exercises;
    private final OnExerciseSelectedListener listener;

    public interface OnExerciseSelectedListener {
        void onExerciseSelected(ExerciseWithProgress exercise);
    }

    public ExerciseListAdapter(List<ExerciseWithProgress> exercises, OnExerciseSelectedListener listener) {
        this.exercises = exercises;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExerciseListBinding binding = ItemExerciseListBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ExerciseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        ExerciseWithProgress exercise = exercises.get(position);
        holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return exercises != null ? exercises.size() : 0;
    }

    /**
     * Update the adapter with a new list of exercises.
     */
    public void updateExercises(List<ExerciseWithProgress> newExercises) {
        this.exercises = newExercises;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for exercise items.
     */
    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final ItemExerciseListBinding binding;

        public ExerciseViewHolder(ItemExerciseListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set click listener for the entire item
            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onExerciseSelected(exercises.get(position));
                }
            });
        }

        public void bind(ExerciseWithProgress exercise) {
            binding.tvExerciseName.setText(exercise.getName());
            binding.tvLastTry.setText(exercise.getLastTryText());
        }
    }
}
