package com.erendogan6.planmyworkout.presentation.plan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.erendogan6.planmyworkout.databinding.FragmentWorkoutPlanDetailBinding;
import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for displaying workout plan details.
 * This is a placeholder fragment that will be implemented in the next phase.
 */
@AndroidEntryPoint
public class WorkoutPlanDetailFragment extends Fragment {

    public static final String ARG_WORKOUT_PLAN = "arg_workout_plan";
    
    private FragmentWorkoutPlanDetailBinding binding;
    private WorkoutPlan workoutPlan;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutPlanDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get workout plan from arguments
        if (getArguments() != null && getArguments().containsKey(ARG_WORKOUT_PLAN)) {
            workoutPlan = (WorkoutPlan) getArguments().getSerializable(ARG_WORKOUT_PLAN);
            
            // Set up toolbar title
            binding.tvToolbarTitle.setText(workoutPlan.getName());
            
            // Display plan details (placeholder)
            Toast.makeText(requireContext(), "Selected plan: " + workoutPlan.getName(), Toast.LENGTH_SHORT).show();
        } else {
            // No plan provided, show error and go back
            Toast.makeText(requireContext(), "Error: No workout plan provided", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }
        
        // Set up back button
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
