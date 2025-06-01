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
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLogHeader;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLogItem;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLogItemWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying exercise logs in a RecyclerView.
 */
public class ExerciseLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ExerciseLogItem> items;
    private final OnLogSelectedListener listener;
    private OnLogActionListener actionListener;

    public interface OnLogSelectedListener {
        void onLogSelected(ExerciseLog log);
    }

    public interface OnLogActionListener {
        void onDeleteLog(ExerciseLog log, int position);
        void onDuplicateLog(ExerciseLog log, int position);
    }

    /**
     * Get item at specific position
     */
    public ExerciseLogItem getItem(int position) {
        if (items != null && position >= 0 && position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    public void removeItem(int position) {
        if (items != null && position >= 0 && position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void restoreItem(ExerciseLogItem item, int position) {
        if (items != null && item != null) {
            items.add(position, item);
            notifyItemInserted(position);
        }
    }

    public ExerciseLogAdapter(List<ExerciseLogItem> items, OnLogSelectedListener listener) {
        this.items = items != null ? items : new ArrayList<>();
        this.listener = listener;
    }

    public void setOnLogActionListener(OnLogActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ExerciseLogItem.TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_exercise_log_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_exercise_log, parent, false);
            return new LogViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExerciseLogItem item = items.get(position);

        if (holder instanceof HeaderViewHolder && item instanceof ExerciseLogHeader) {
            ((HeaderViewHolder) holder).bind((ExerciseLogHeader) item);
        } else if (holder instanceof LogViewHolder && item instanceof ExerciseLogItemWrapper) {
            ((LogViewHolder) holder).bind(((ExerciseLogItemWrapper) item).getLog(), listener);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateLogs(List<ExerciseLogItem> newItems) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void onItemSwiped(int position, int direction) {
        if (items != null && position >= 0 && position < items.size()) {
            ExerciseLogItem item = items.get(position);

            // Only allow swiping on log items, not headers
            if (item instanceof ExerciseLogItemWrapper && actionListener != null) {
                ExerciseLog log = ((ExerciseLogItemWrapper) item).getLog();

                if (direction == ItemTouchHelper.LEFT) {
                    actionListener.onDeleteLog(log, position);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    actionListener.onDuplicateLog(log, position);
                }
            }
        }
    }

    // Header ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvHeaderDate;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeaderDate = itemView.findViewById(R.id.tvHeaderDate);
        }

        public void bind(ExerciseLogHeader header) {
            tvHeaderDate.setText(header.getDate());
        }
    }

    // Log ViewHolder
    static class LogViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTime;
        private final TextView tvReps;
        private final TextView tvWeight;
        private final TextView tvNotes;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvReps = itemView.findViewById(R.id.tvReps);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvNotes = itemView.findViewById(R.id.tvNotes);
        }

        public void bind(ExerciseLog log, OnLogSelectedListener listener) {
            // Set time only (extract from formatted date)
            tvTime.setText(log.getFormattedTime()); // You'll need to add this method

            // Set reps and weight
            tvReps.setText(String.valueOf(log.getReps()));
            tvWeight.setText(String.valueOf((int) log.getWeight()));

            // Show notes if available
            if (log.getNotes() != null && !log.getNotes().trim().isEmpty()) {
                tvNotes.setVisibility(View.VISIBLE);
                tvNotes.setText(log.getNotes());
            } else {
                tvNotes.setVisibility(View.GONE);
            }

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