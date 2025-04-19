package com.erendogan6.planmyworkout.presentation.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.databinding.ActivityOnboardingBinding;
import com.erendogan6.planmyworkout.domain.model.OnboardingChoice;
import com.erendogan6.planmyworkout.presentation.plan.AiPlanGenerationActivity;
import com.erendogan6.planmyworkout.presentation.plan.CreatePlanActivity;
import com.erendogan6.planmyworkout.presentation.plan.ReadyMadePlansActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for user onboarding.
 * This activity allows the user to choose how they want to start using the app.
 */
@AndroidEntryPoint
public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up click listeners directly
        binding.cardReadyMade.setOnClickListener(v -> {
            Toast.makeText(this, "Ready-made card clicked", Toast.LENGTH_SHORT).show();
            navigateBasedOnChoice(OnboardingChoice.READY_MADE);
        });

        binding.cardManual.setOnClickListener(v -> {
            Toast.makeText(this, "Manual card clicked", Toast.LENGTH_SHORT).show();
            navigateBasedOnChoice(OnboardingChoice.MANUAL);
        });

        binding.cardAi.setOnClickListener(v -> {
            Toast.makeText(this, "AI card clicked", Toast.LENGTH_SHORT).show();
            navigateBasedOnChoice(OnboardingChoice.AI);
        });
    }

    private void navigateBasedOnChoice(OnboardingChoice choice) {
        Intent intent = null;

        switch (choice) {
            case READY_MADE:
                intent = new Intent(this, ReadyMadePlansActivity.class);
                break;
            case MANUAL:
                intent = new Intent(this, CreatePlanActivity.class);
                break;
            case AI:
                intent = new Intent(this, AiPlanGenerationActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }
}
