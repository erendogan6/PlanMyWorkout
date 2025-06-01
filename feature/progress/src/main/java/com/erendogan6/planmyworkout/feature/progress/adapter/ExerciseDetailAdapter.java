package com.erendogan6.planmyworkout.feature.progress.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erendogan6.planmyworkout.feature.progress.R;
import com.erendogan6.planmyworkout.feature.progress.model.ExerciseDetail;
import com.erendogan6.planmyworkout.feature.progress.model.SetDetail;

import java.util.List;

/**
 * Adapter for displaying exercise details in the progress screen.
 */
public class ExerciseDetailAdapter extends RecyclerView.Adapter<ExerciseDetailAdapter.ExerciseViewHolder> {

    private List<ExerciseDetail> exercises;

    public ExerciseDetailAdapter(List<ExerciseDetail> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise_detail, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        ExerciseDetail exercise = exercises.get(position);
        holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void updateExercises(List<ExerciseDetail> newExercises) {
        this.exercises = newExercises;
        notifyDataSetChanged();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvExerciseName;
        private final TextView tvSetDetails;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvSetDetails = itemView.findViewById(R.id.tvSetDetails);
        }

        public void bind(ExerciseDetail exercise) {
            tvExerciseName.setText(exercise.getExerciseName());

            // Format set details
            StringBuilder setDetails = new StringBuilder();
            List<SetDetail> sets = exercise.getSets();

            if (sets != null && !sets.isEmpty()) {
                // Group sets by reps if they're the same
                int currentReps = sets.get(0).getReps();
                StringBuilder weights = new StringBuilder();
                weights.append(String.format("%.1f", sets.get(0).getWeight()));

                for (int i = 1; i < sets.size(); i++) {
                    SetDetail set = sets.get(i);
                    if (set.getReps() == currentReps) {
                        weights.append(", ").append(String.format("%.1f", set.getWeight()));
                    } else {
                        // Different reps, finish current group and start new one
                        if (setDetails.length() > 0) setDetails.append("\n");
                        setDetails.append(currentReps).append(" rep: ").append(weights.toString()).append(" kg");

                        currentReps = set.getReps();
                        weights = new StringBuilder();
                        weights.append(String.format("%.1f", set.getWeight()));
                    }
                }

                // Add the last group
                if (setDetails.length() > 0) setDetails.append("\n");
                setDetails.append(currentReps).append(" rep: ").append(weights.toString()).append(" kg");
            } else {
                setDetails.append("No sets recorded");
            }

            tvSetDetails.setText(setDetails.toString());
        }
    }
}