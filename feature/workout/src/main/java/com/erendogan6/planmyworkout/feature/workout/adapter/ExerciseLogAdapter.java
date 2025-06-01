package com.erendogan6.planmyworkout.feature.workout.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private OnLogActionListener actionListener;

    /**
     * Interface for handling log selection events.
     */
    public interface OnLogSelectedListener {
        void onLogSelected(ExerciseLog log);
    }

    /**
     * Interface for handling swipe actions.
     */
    public interface OnLogActionListener {
        void onDeleteLog(ExerciseLog log, int position);
        void onDuplicateLog(ExerciseLog log, int position);
    }

    public ExerciseLogAdapter(List<ExerciseLog> logs, OnLogSelectedListener listener) {
        this.logs = logs;
        this.listener = listener;
    }

    public void setOnLogActionListener(OnLogActionListener actionListener) {
        this.actionListener = actionListener;
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
     * Remove item for swipe action
     */
    public void removeItem(int position) {
        if (logs != null && position >= 0 && position < logs.size()) {
            logs.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Restore item for undo action
     */
    public void restoreItem(ExerciseLog log, int position) {
        if (logs != null) {
            logs.add(position, log);
            notifyItemInserted(position);
        }
    }

    /**
     * Handle swipe actions
     */
    public void onItemSwiped(int position, int direction) {
        if (logs != null && position >= 0 && position < logs.size()) {
            ExerciseLog log = logs.get(position);

            if (actionListener != null) {
                if (direction == ItemTouchHelper.LEFT) {
                    actionListener.onDeleteLog(log, position);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    actionListener.onDuplicateLog(log, position);
                }
            }
        }
    }

    /**
     * ViewHolder for exercise logs.
     */
    class LogViewHolder extends RecyclerView.ViewHolder {
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
            if (log.getNotes() != null && !log.getNotes().trim().isEmpty()) {
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
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onLogSelected(log);
                    }
                }
            });
        }
    }
}