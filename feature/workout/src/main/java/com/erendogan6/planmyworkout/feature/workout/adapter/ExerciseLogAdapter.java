package com.erendogan6.planmyworkout.feature.workout.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erendogan6.planmyworkout.feature.workout.R;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLog;

import java.util.List;

/**
 * Adapter for displaying exercise logs in a RecyclerView.
 */
public class ExerciseLogAdapter extends RecyclerView.Adapter<ExerciseLogAdapter.LogViewHolder> {

    private List<ExerciseLog> logs;
    private final OnLogSelectedListener listener;

    /**
     * Interface for handling log selection events.
     */
    public interface OnLogSelectedListener {
        void onLogSelected(ExerciseLog log);
    }

    public ExerciseLogAdapter(List<ExerciseLog> logs, OnLogSelectedListener listener) {
        this.logs = logs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise_log, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        ExerciseLog log = logs.get(position);
        holder.bind(log, listener);
    }

    @Override
    public int getItemCount() {
        return logs != null ? logs.size() : 0;
    }

    /**
     * Update the logs in the adapter.
     */
    public void updateLogs(List<ExerciseLog> newLogs) {
        this.logs = newLogs;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for exercise logs.
     */
    static class LogViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvWeightReps;
        private final TextView tvNotes;
        private final View divider;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvWeightReps = itemView.findViewById(R.id.tvWeightReps);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            divider = itemView.findViewById(R.id.divider);
        }

        public void bind(ExerciseLog log, OnLogSelectedListener listener) {
            tvDate.setText(log.getFormattedDate());
            tvWeightReps.setText(String.format("%.1f kg × %d", log.getWeight(), log.getReps()));

            // Show notes if available
            if (log.getNotes() != null && !log.getNotes().isEmpty()) {
                divider.setVisibility(View.VISIBLE);
                tvNotes.setVisibility(View.VISIBLE);
                tvNotes.setText(log.getNotes());
            } else {
                divider.setVisibility(View.GONE);
                tvNotes.setVisibility(View.GONE);
            }

            // Add ripple effect animation
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLogSelected(log);
                }
            });
        }
    }
}
