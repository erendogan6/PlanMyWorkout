package com.erendogan6.planmyworkout.presentation.plan;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.erendogan6.planmyworkout.R;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for displaying ready-made workout plans.
 * This is a placeholder activity that will be implemented in the next phase.
 */
@AndroidEntryPoint
public class ReadyMadePlansActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_made_plans);
        
        // This is a placeholder activity
        Toast.makeText(this, "Ready-Made Plans Screen - Coming Soon", Toast.LENGTH_SHORT).show();
    }
}
