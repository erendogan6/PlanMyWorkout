package com.erendogan6.planmyworkout.feature.onboarding.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erendogan6.planmyworkout.feature.onboarding.R;
import com.erendogan6.planmyworkout.feature.onboarding.model.AiWorkoutPlanResponse;

import java.util.List;

public class AiExercisePreviewAdapter extends RecyclerView.Adapter<AiExercisePreviewAdapter.ExerciseViewHolder> {

    private final List<AiWorkoutPlanResponse.AiExercise> exercises;

    public AiExercisePreviewAdapter(List<AiWorkoutPlanResponse.AiExercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ai_exercise_preview, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        AiWorkoutPlanResponse.AiExercise exercise = exercises.get(position);
        holder.bind(exercise, position + 1);
    }

    @Override
    public int getItemCount() {
        return exercises != null ? exercises.size() : 0;
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvExerciseNumber;
        private final TextView tvExerciseName;
        private final TextView tvMuscleGroup;
        private final TextView tvDescription;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseNumber = itemView.findViewById(R.id.tvExerciseNumber);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvMuscleGroup = itemView.findViewById(R.id.tvMuscleGroup);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(AiWorkoutPlanResponse.AiExercise exercise, int number) {
            tvExerciseNumber.setText(String.valueOf(number));
            tvExerciseName.setText(exercise.getName());
            tvMuscleGroup.setText(exercise.getMuscleGroup());
            tvDescription.setText(exercise.getDescription());
        }
    }
}