package com.erendogan6.planmyworkout.feature.onboarding.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erendogan6.planmyworkout.feature.onboarding.databinding.ItemWorkoutPlanBinding;
import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;

import java.util.List;

/**
 * Adapter for displaying workout plans in a RecyclerView.
 * Uses ViewBinding for cleaner view access.
 */
public class WorkoutPlanAdapter extends RecyclerView.Adapter<WorkoutPlanAdapter.WorkoutPlanViewHolder> {

    private List<WorkoutPlan> workoutPlans;
    private OnPlanSelectedListener listener;

    /**
     * Interface for handling plan selection events.
     */
    public interface OnPlanSelectedListener {
        void onPlanSelected(WorkoutPlan plan);
    }

    public WorkoutPlanAdapter(List<WorkoutPlan> workoutPlans, OnPlanSelectedListener listener) {
        this.workoutPlans = workoutPlans;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWorkoutPlanBinding binding = ItemWorkoutPlanBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkoutPlanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutPlanViewHolder holder, int position) {
        WorkoutPlan plan = workoutPlans.get(position);
        holder.bind(plan);
    }

    @Override
    public int getItemCount() {
        return workoutPlans != null ? workoutPlans.size() : 0;
    }

    /**
     * Update the adapter with a new list of workout plans.
     * @param newPlans The new list of workout plans
     */
    public void updatePlans(List<WorkoutPlan> newPlans) {
        this.workoutPlans = newPlans;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for workout plan items.
     * Uses ViewBinding for cleaner view access.
     */
    class WorkoutPlanViewHolder extends RecyclerView.ViewHolder {
        private final ItemWorkoutPlanBinding binding;

        public WorkoutPlanViewHolder(ItemWorkoutPlanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Make the entire card clickable
            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onPlanSelected(workoutPlans.get(position));
                }
            });

            // Also make the button clickable
            binding.btnSelectPlan.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onPlanSelected(workoutPlans.get(position));
                }
            });
        }

        public void bind(WorkoutPlan plan) {
            binding.tvPlanName.setText(plan.getName());
            binding.tvPlanDescription.setText(plan.getDescription());

            // Format the details text
            String details = "Difficulty: " + plan.getDifficulty() +
                    " • " + plan.getDaysPerWeek() + " days/week" +
                    " • " + plan.getDurationWeeks() + " weeks";
            binding.tvPlanDetails.setText(details);
        }
    }
}
