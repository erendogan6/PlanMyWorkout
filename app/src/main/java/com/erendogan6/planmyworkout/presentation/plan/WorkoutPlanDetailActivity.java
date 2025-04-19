package com.erendogan6.planmyworkout.presentation.plan;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.databinding.ActivityWorkoutPlanDetailBinding;
import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for displaying workout plan details.
 * This is a placeholder activity that will be implemented in the next phase.
 */
@AndroidEntryPoint
public class WorkoutPlanDetailActivity extends AppCompatActivity {

    public static final String EXTRA_WORKOUT_PLAN = "extra_workout_plan";
    
    private ActivityWorkoutPlanDetailBinding binding;
    private WorkoutPlan workoutPlan;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize ViewBinding
        binding = ActivityWorkoutPlanDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Get workout plan from intent
        if (getIntent() != null && getIntent().hasExtra(EXTRA_WORKOUT_PLAN)) {
            workoutPlan = (WorkoutPlan) getIntent().getSerializableExtra(EXTRA_WORKOUT_PLAN);
            
            // Display plan details (placeholder)
            Toast.makeText(this, "Selected plan: " + workoutPlan.getName(), Toast.LENGTH_SHORT).show();
        } else {
            // No plan provided, show error and finish
            Toast.makeText(this, "Error: No workout plan provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
