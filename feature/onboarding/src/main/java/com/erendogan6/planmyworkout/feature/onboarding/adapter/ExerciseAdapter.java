package com.erendogan6.planmyworkout.feature.onboarding.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erendogan6.planmyworkout.feature.onboarding.databinding.ItemExerciseBinding;
import com.erendogan6.planmyworkout.feature.onboarding.model.Exercise;

import java.util.List;

/**
 * Adapter for displaying exercises in a RecyclerView.
 * Uses ViewBinding for cleaner view access.
 */
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exercises;

    public ExerciseAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExerciseBinding binding = ItemExerciseBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ExerciseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return exercises != null ? exercises.size() : 0;
    }

    /**
     * Update the adapter with a new list of exercises.
     * @param newExercises The new list of exercises
     */
    public void updateExercises(List<Exercise> newExercises) {
        this.exercises = newExercises;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for exercise items.
     * Uses ViewBinding for cleaner view access.
     */
    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final ItemExerciseBinding binding;

        public ExerciseViewHolder(ItemExerciseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Exercise exercise) {
            binding.tvExerciseName.setText(exercise.getName());
            binding.tvExerciseDescription.setText(exercise.getDescription());
            binding.tvMuscleGroup.setText(exercise.getMuscleGroup());

            // Format the sets and reps text with the unit
            String unit = exercise.getUnit() != null ? exercise.getUnit() : "reps";
            String setsReps = exercise.getSets() + " sets × " + exercise.getRepsPerSet() + " " + unit;
            binding.tvSetsReps.setText(setsReps);

            // Format the rest time text
            String restTime = exercise.getRestSeconds() + " sec rest";
            binding.tvRestTime.setText(restTime);
        }
    }
}
