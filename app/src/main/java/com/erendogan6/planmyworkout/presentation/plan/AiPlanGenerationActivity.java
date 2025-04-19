package com.erendogan6.planmyworkout.presentation.plan;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.erendogan6.planmyworkout.R;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for generating a workout plan using AI.
 * This is a placeholder activity that will be implemented in the next phase.
 */
@AndroidEntryPoint
public class AiPlanGenerationActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_plan_generation);
        
        // This is a placeholder activity
        Toast.makeText(this, "AI Plan Generation Screen - Coming Soon", Toast.LENGTH_SHORT).show();
    }
}
