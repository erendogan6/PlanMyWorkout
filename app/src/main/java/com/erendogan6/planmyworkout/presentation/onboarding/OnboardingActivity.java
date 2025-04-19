package com.erendogan6.planmyworkout.presentation.onboarding;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.erendogan6.planmyworkout.R;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for user onboarding.
 * This is a placeholder activity that will be implemented in the next phase.
 */
@AndroidEntryPoint
public class OnboardingActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
    }
}
