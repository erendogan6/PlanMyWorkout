package com.erendogan6.planmyworkout.feature.onboarding.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erendogan6.planmyworkout.feature.onboarding.R;
import com.erendogan6.planmyworkout.feature.onboarding.model.ExerciseTemplate;

import java.util.ArrayList;
import java.util.List;

public class ExerciseTemplateAdapter extends RecyclerView.Adapter<ExerciseTemplateAdapter.ExerciseTemplateViewHolder> {

    private List<ExerciseTemplate> templates;
    private final OnExerciseTemplateClickListener listener;

    public interface OnExerciseTemplateClickListener {
        void onExerciseTemplateClick(ExerciseTemplate template);
    }

    public ExerciseTemplateAdapter(OnExerciseTemplateClickListener listener) {
        this.templates = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseTemplateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise_template, parent, false);
        return new ExerciseTemplateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseTemplateViewHolder holder, int position) {
        ExerciseTemplate template = templates.get(position);
        holder.bind(template, listener);
    }

    @Override
    public int getItemCount() {
        return templates.size();
    }

    public void updateTemplates(List<ExerciseTemplate> newTemplates) {
        this.templates = newTemplates != null ? newTemplates : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ExerciseTemplateViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvExerciseName;
        private final TextView tvMuscleGroup;
        private final TextView tvDescription;
        private final TextView tvDefaults;

        public ExerciseTemplateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvMuscleGroup = itemView.findViewById(R.id.tvMuscleGroup);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDefaults = itemView.findViewById(R.id.tvDefaults);
        }

        public void bind(ExerciseTemplate template, OnExerciseTemplateClickListener listener) {
            tvExerciseName.setText(template.getName());
            tvMuscleGroup.setText(template.getMuscleGroup());
            tvDescription.setText(template.getDescription());
            tvDefaults.setText(String.format("%d sets × %d reps",
                    template.getDefaultSets(), template.getDefaultReps()));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onExerciseTemplateClick(template);
                }
            });
        }
    }
}